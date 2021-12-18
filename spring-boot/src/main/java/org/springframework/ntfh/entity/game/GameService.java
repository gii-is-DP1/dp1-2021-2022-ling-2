package org.springframework.ntfh.entity.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.enemy.hordeenemy.ingame.HordeEnemyIngameService;
import org.springframework.ntfh.entity.enemy.warlord.ingame.WarlordIngameService;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private HordeEnemyIngameService hordeEnemyIngameService;

    @Autowired
    private WarlordIngameService warlordIngameService;

    @Autowired
    private MarketCardIngameService marketCardIngameService;

    @Autowired
    private TurnService turnService;

    @Transactional
    public Integer gameCount() {
        return (int) gameRepo.count();
    }

    public Iterable<Game> findAll() {
        return gameRepo.findAll();
    }

    @Transactional
    public Game findGameById(int id) throws DataAccessException {
        Optional<Game> game = gameRepo.findById(id);
        if (!game.isPresent())
            throw new DataAccessException("Game with id " + id + " was not found") {
            };
        return game.get();
    }

    @Transactional
    public Game createFromLobby(@Valid Lobby lobby) {
        if (lobby.getUsers().size() < 2) {
            throw new IllegalArgumentException("A game must have at least 2 players");
        }
        Game game = new Game();
        game.setStartTime(System.currentTimeMillis());
        game.setHasScenes(lobby.getHasScenes());

        Set<User> users = lobby.getUsers();

        Integer i = 1;
        List<Player> players = new ArrayList<>();
        for (User user : users) {
            // TODO test
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
        Game savedGame = gameRepo.save(game);

        // Now, we instantiate the entities that will be used in the game
        // TODO should these be created on the turn? some of them? I dont know
        hordeEnemyIngameService.createFromGame(game);
        warlordIngameService.createFromGame(game);
        marketCardIngameService.createFromGame(game);
        turnService.createFromGame(game);

        // Once the game is in the database, we update the lobby with a FK to it
        lobby.setGame(game);
        lobbyService.save(lobby);
        return savedGame;
    }

    @Transactional
    public Game save(@Valid Game game) {
        // Return the game created after saving it
        return gameRepo.save(game);
    }

    public void delete(Game game) {
        gameRepo.delete(game);
    }

}
