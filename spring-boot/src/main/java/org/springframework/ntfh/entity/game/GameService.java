package org.springframework.ntfh.entity.game;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.entity.game.concretestates.FinishedState;
import org.springframework.ntfh.entity.game.concretestates.LobbyState;
import org.springframework.ntfh.entity.game.concretestates.OngoingState;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TurnService turnService;

    /************ STATES ************/

    @Autowired
    private LobbyState lobbyState;

    @Autowired
    private OngoingState ongoingState;

    @Autowired
    private FinishedState finishedState;

    /*******************************/

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

    public Integer gameCount() {
        return (int) gameRepository.count();
    }

    public Integer countByStateType(GameStateType stateType) {
        return (int) gameRepository.countByStateType(stateType);
    }

    public Iterable<Game> findByStateType(GameStateType stateType) {
        return gameRepository.findByStateType(stateType, Pageable.unpaged()).getContent();
    }

    public Iterable<Game> findByStateTypePageable(GameStateType stateType, Pageable pageable) {
        return gameRepository.findByStateType(stateType, pageable).getContent();
    }

    public Iterable<Game> findFinishedByUser(User user, Pageable pageable) {
        return gameRepository.findFinishedByUser(user, GameStateType.FINISHED, pageable).getContent();
    }

    public Turn getCurrentTurnByGameId(Integer gameId) {
        List<Turn> turns = gameRepository.getTurnsByGameId(gameId);
        return turns.isEmpty() ? null : turns.get(turns.size() - 1);
    }

    @Transactional
    public Game createGame(Game game) {
        // Security measure to ensure that only the requested properties are set by the
        // sender
        Game newGame = new Game();
        newGame.setName(game.getName());
        newGame.setHasScenes(game.getHasScenes());
        newGame.setSpectatorsAllowed(game.getSpectatorsAllowed());
        newGame.setMaxPlayers(game.getMaxPlayers());

        newGame.setStateType(GameStateType.LOBBY);
        return this.save(newGame);
    }

    /**
     * Adds the given player to the list of players in the lobby.
     * 
     * @author andrsdt
     * @param lobbyId
     * @param usernameFromRequest username that will be added to the lobby
     * @param token JWT token sent by the client
     * @return true if the player was added, false if there was some problem
     */
    @Transactional
    public Game joinGame(Game game, User user) throws DataAccessException, MaximumLobbyCapacityException {
        GameState gameState = this.getState(game);
        return gameState.joinGame(game, user);
    }

    @Transactional
    public Game removePlayer(Integer gameId, String username, String token) {
        Game game = this.findGameById(gameId);
        String tokenUsername = TokenUtils.usernameFromToken(token);

        Boolean sentByHost = game.getLeader().getUser().getUsername().equals(tokenUsername);
        Boolean sentByPlayerLeaving = username.equals(tokenUsername);

        if (Boolean.FALSE.equals(sentByHost) && Boolean.FALSE.equals(sentByPlayerLeaving)) {
            log.error("User " + tokenUsername + " tried to remove player " + username + " from game " + gameId);
            throw new NonMatchingTokenException(
                    "A user can only leave a lobby by himself or by being kicked by the leader") {};
        }

        GameState gameState = this.getState(game);
        return gameState.removePlayer(gameId, username);
    }

    @Transactional
    public Game save(Game game) {
        // Return the game created after saving it
        return gameRepository.save(game);
    }

    @Transactional
    public void delete(Game game) {
        gameRepository.delete(game);
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
    public Game finishGame(Game game) {
        GameState gameState = this.getState(game);
        return gameState.finishGame(game);
    }

    @Transactional
    public Game startGame(Integer gameId) {
        Game game = this.findGameById(gameId);
        game.setStartTime(Timestamp.from(Instant.now()));
        GameState gameState = this.getState(game);
        return gameState.startGame(gameId);
    }

    public GameState getState(Game game) {
        GameStateType stateType = game.getStateType();
        if (stateType == GameStateType.LOBBY) {
            return lobbyState;
        } else if (stateType == GameStateType.ONGOING) {
            return ongoingState;
        } else if (stateType == GameStateType.FINISHED) {
            return finishedState;
        } else {
            return null;
        }
    }

    @Transactional
    public void setNextState(Game game) {
        GameState state = getState(game);
        GameStateType nextState = state.getNextState();
        game.setStateType(nextState);

        GameState newState = getState(game);
        newState.preState(game); // Execute the preState method right after setting the new state
    }
}
