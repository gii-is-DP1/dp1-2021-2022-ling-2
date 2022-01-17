package org.springframework.ntfh.entity.game;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private TurnService turnService;

    public Integer gameCount() {
        return (int) gameRepository.count();
    }

    public Iterable<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findGameById(int id) throws DataAccessException {
        Optional<Game> game = gameRepository.findById(id);
        if (!game.isPresent()) {
            log.error("Game with id " + id + " was not found");
            throw new DataAccessException("Game with id " + id + " was not found") {};
        }
        return game.get();
    }

    public List<Player> findPlayersByGameId(int id) throws DataAccessException {
        return gameRepository.getPlayersByGameId(id);
    }

    public Turn getCurrentTurnByGameId(Integer gameId) {
        List<Turn> turns = gameRepository.getTurnsByGameId(gameId);
        return turns.isEmpty() ? null : turns.get(turns.size() - 1);
    }

    @Transactional
    public Game createFromLobby(@Valid Lobby lobby) {
        Game game = new Game();
        game.setStartTime(Timestamp.from(Instant.now()));
        game.setHasScenes(lobby.getHasScenes());

        // We have to use lobbyFromDB since the one from the request does not
        // contain some user attributes and will set them to null later
        Lobby lobbyFromDB = lobbyService.findById(lobby.getId());
        Set<User> users = lobbyFromDB.getUsers();

        Integer i = 1;
        List<Player> players = new ArrayList<>();
        for (User user : users) {
            // The turnOrder 0 is reserved for the host
            Boolean isHost = lobby.getHost().getUsername().equals(user.getUsername());
            Integer turnOrder = isHost ? 0 : i;
            if (!isHost)
                i++;
            Player createdPlayer = playerService.createFromUser(user, lobby, turnOrder);

            players.add(createdPlayer);
            // TODO temporary solution. Set the lobby host as the leader. In the real game
            // it is chosen via a "minigame" with cards
            if (isHost)
                game.setLeader(createdPlayer);
        }

        game.setPlayers(players);
        Game savedGame = gameRepository.save(game);
        // Now, we instantiate the entities that will be used in the game

        turnService.initializeFromGame(game);

        // Once the game is in the database, we update the lobby with a FK to it
        lobby.setGame(game);
        lobbyService.save(lobby);
        log.info(
                "Game with id " + game.getId() + " was created with players: " + game.getPlayers());
        return savedGame;
    }

    @Transactional
    public Game save(@Valid Game game) {
        // Return the game created after saving it
        return gameRepository.save(game);
    }

    @Transactional
    public void delete(Game game) {
        gameRepository.delete(game);
    }

    @Transactional
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
        // TODO make getting the turn more straightforward, maybe with a custom query
        String username = TokenUtils.usernameFromToken(token);
        Player player = userService.findUser(username).getPlayer();
        Turn currentTurn = player.getGame().getCurrentTurn();
        TurnState turnState = turnService.getState(currentTurn);
        turnState.playCard(abilityCardIngameId, enemyId, token);
    }

    /**
     * Executed when a player tries to buy a market card
     * 
     * @param marketCardIngameId
     */
    @Transactional
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        // TODO make getting the turn more straightforward, maybe with a custom query
        String username = TokenUtils.usernameFromToken(token);
        Player player = userService.findUser(username).getPlayer();
        Turn currentTurn = player.getGame().getCurrentTurn();
        TurnState turnState = turnService.getState(currentTurn);
        turnState.buyMarketCard(marketCardIngameId, token);
    }

    @Transactional
    public void setNextTurnState(Turn turn) {
        turnService.setNextState(turn);
    }

    /**
     * Executed then some conditions lead to the finish of the game
     * 
     * @author andrsdt
     */
    @Transactional
    public void finishGame(Game game) {
        game.setFinishTime(Timestamp.from(Instant.now()));
        game.getPlayers().forEach(p -> {
            User user = p.getUser();
            user.setLobby(null);
            user.setPlayer(null);
            user.setCharacter(null);
        });
    }
}
