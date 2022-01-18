package org.springframework.ntfh.entity.game;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.game.concretestates.FinishedState;
import org.springframework.ntfh.entity.game.concretestates.LobbyState;
import org.springframework.ntfh.entity.game.concretestates.OngoingState;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.exceptions.MaximumLobbyCapacityException;
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
	private TurnService turnService;

	/************ STATES ************/

	@Autowired
	private LobbyState lobbyState;

	@Autowired
	private OngoingState ongoingState;

	@Autowired
	private FinishedState finishedState;

	/*******************************/

	public Integer gameCount() {
		return (int) gameRepository.count();
	}

	public Iterable<Game> findAll() {
		return gameRepository.findAll();
	}

	public Game findGameById(int id) throws DataAccessException {
		Optional<Game> game = gameRepository.findById(id);
		if (!game.isPresent()) {
			log.error("game with id " + id + " was not found");
			throw new DataAccessException("game with id " + id + " was not found") {};
		}
		return game.get();
	}

	public List<Player> findPlayersByGameId(int id) throws DataAccessException {
		return gameRepository.getPlayersByGameId(id);
	}

	public Turn getCurrentTurnByGameId(Integer gameId) {
		// TODO move to TurnService?
		List<Turn> turns = gameRepository.getTurnsByGameId(gameId);
		return turns.isEmpty() ? null : turns.get(turns.size() - 1);
	}

	@Transactional
	public Game createGame(Game game) {
		// Security measure to ensure that only the requested properties are set by the requester
		Game newGame = new Game();
		newGame.setName(game.getName());
		newGame.setHasScenes(game.getHasScenes());
		newGame.setSpectatorsAllowed(game.getSpectatorsAllowed());
		newGame.setMaxPlayers(game.getMaxPlayers());

		newGame.setStateType(GameStateType.LOBBY);
		return this.save(newGame);
	}

	@Transactional
	public void deleteGame(Integer gameId) {
		// TODO If the petition is sent by a user, only allow to delete it if is in lobby and token
		// coincides
		// TODO make sure to clear the FKs in Users, cascade delete players and all that
		// ! delegate to state as the rest of methods
		Game game = this.findGameById(gameId);
		game.getPlayers().forEach(p -> p.getUser().setPlayer(null)); // TODO player remains orfan
		this.delete(game);
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
	public Game joinGame(Integer gameId, String username, String token)
			throws DataAccessException, MaximumLobbyCapacityException {
		Game game = this.findGameById(gameId);
		GameState gameState = this.getState(game);
		return gameState.joinGame(gameId, username, token);
	}

	@Transactional
	public Game removePlayer(Integer gameId, String username, String token) {
		// TODO make sure that the one trying to remove the player is either the player himself or the host
		// ! delegate to state as the rest of methods

		Game game = this.findGameById(gameId);
		GameState gameState = this.getState(game);
		return gameState.removePlayer(gameId, username, token);
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
	public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
		// TODO make getting the turn more straightforward, maybe with a custom query
		// TODO handle token check here instead of in concreteState
		String username = TokenUtils.usernameFromToken(token);
		Player player = userService.findUser(username).getPlayer();
		Game game = player.getGame();
		GameState gameState = this.getState(game);
		gameState.playCard(abilityCardIngameId, enemyId, token);
	}

	/**
	 * Executed when a player tries to buy a market card
	 * 
	 * @param marketCardIngameId
	 */
	@Transactional
	public void buyMarketCard(Integer marketCardIngameId, String token) {
		// TODO make getting the turn more straightforward, maybe with a custom query
		// TODO handle token check here instead of in concreteState
		String username = TokenUtils.usernameFromToken(token);
		Player player = userService.findUser(username).getPlayer();
		Game game = player.getGame();
		GameState gameState = this.getState(game);
		gameState.buyMarketCard(marketCardIngameId, token);
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
		GameState gameState = this.getState(game);
		gameState.finishGame(game);
	}

	@Transactional
	public Game startGame(Integer gameId) {
		// ! delegate to state as the rest of methods
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
