package org.springframework.ntfh.entity.game;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

/**
 * @author andrsdt
 */
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * @return List of all the games ever played
     * @author pabrobcam
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Game> getAll() {
        return gameService.findAll();
    }

    @GetMapping("count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCount() {
        return gameService.gameCount();
    }

    @GetMapping("history")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Game> getPastGames() {
        // TODO implement
        return gameService.findAll();
    }

    @GetMapping("history/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getPastGamesCount() {
        // TODO implement
        return gameService.gameCount();
    }

    @GetMapping("{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public Game getGame(@PathVariable("gameId") Integer gameId) {
        return gameService.findGameById(gameId);
    }

    /**
     * This endpoint handles the creation of a new game from a lobby
     * 
     * @param lobby object with the preferences for the game
     * @return id of the game so the user can be redirected from the lobby
     * @author andrsdt
     */
    @PostMapping("new")
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody Game game) throws IllegalArgumentException {
        // TODO check here the input validations (e.g. spectatorsAllowed is not null)
        // if they are not checked in hte entity
        return gameService.createGame(game);
    }

    /**
     * This endpoint handles the creation of a new game from a lobby
     * 
     * @param lobby object with the preferences for the game
     * @return id of the game so the user can be redirected from the lobby
     * @author andrsdt
     */
    @DeleteMapping("{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteGame(@PathVariable("gameId") Integer gameId) {
        gameService.deleteGame(gameId);
    }

    /**
     * This endpoint will be called from the user's browser to join a game lobby. If the response is
     * 200 OK (The game hasn't started yet and there is still space), then he/she will be redirected
     * to the lobby
     * 
     * @param lobbyId id of the lobby that the user wants to join to
     * @param token JWT token of the user, to validate server-side that the user who wants to join
     *        is the one who is logged in
     * @return 200 OK if the game hasn't started yet and there is still space
     * @return 404 NOT FOUND if the lobby doesn't exist
     * @return 403 FORBIDDEN if the game has already started, the players is already in the game or
     *         there is no room left
     * @return 401 UNAUTHORIZED if the user who sent the request is not logged or is not the one who
     *         he/she claims to be (JWT token validation)
     * @author andrsdt
     */
    @PostMapping("{gameId}/add/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Game joinGame(@PathVariable("gameId") Integer gameId,
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String token) {
        if (!TokenUtils.usernameCoincidesWithToken(username, token)) {
            throw new NonMatchingTokenException(
                    "The user who is trying to join the game is not the one logged in");
        }
        Game game = gameService.joinGame(gameId, username);
        log.info("User " + username + " joined game with id " + gameId);
        return game;
    }

    @PostMapping("{gameId}/remove/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Game removeFromGame(@PathVariable("gameId") Integer gameId,
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String token) {
        Game game = gameService.removePlayer(gameId, username, token);
        log.info("User " + username + " was removed from game " + gameId);
        return game;
    }

    @PostMapping("{gameId}/start")
    @ResponseStatus(HttpStatus.OK)
    public Game startGame(@PathVariable("gameId") Integer gameId,
            @RequestHeader("Authorization") String token) {
        Game game = gameService.findGameById(gameId);
        if (game.getPlayers().size() < 2) {
            throw new IllegalArgumentException("Not enough players to start the game");
        }
        return gameService.startGame(gameId);
    }

    @GetMapping("{gameId}/turn")
    @ResponseStatus(HttpStatus.OK)
    public Turn getTurn(@PathVariable("gameId") Integer gameId) {
        return gameService.getCurrentTurnByGameId(gameId);
    }

    @PostMapping("{gameId}/turn/next")
    @ResponseStatus(HttpStatus.OK)
    public Game nextTurn(@PathVariable("gameId") Integer gameId) {
        Turn turn = gameService.getCurrentTurnByGameId(gameId);
        gameService.setNextTurnState(turn);
        return gameService.findGameById(gameId);
    }
}
