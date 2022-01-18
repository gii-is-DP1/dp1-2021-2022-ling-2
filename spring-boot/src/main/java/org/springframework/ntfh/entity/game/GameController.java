package org.springframework.ntfh.entity.game;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.turn.Turn;
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
    public ResponseEntity<Iterable<Game>> getAll() {
        Iterable<Game> games = gameService.findAll();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("history")
    public ResponseEntity<Iterable<Game>> getPastGames() {
        // TODO implement
        Iterable<Game> games = gameService.findAll();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("history/count")
    public ResponseEntity<Integer> getPastGamesCount() {
        // TODO implement
        Integer count = gameService.gameCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("count")
    public ResponseEntity<Integer> getCount() {
        Integer count = gameService.gameCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable("gameId") Integer gameId) {
        Game game = gameService.findGameById(gameId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    /**
     * This endpoint handles the creation of a new game from a lobby
     * 
     * @param lobby object with the preferences for the game
     * @return id of the game so the user can be redirected from the lobby
     * @author andrsdt
     */
    @PostMapping("new")
    public ResponseEntity<Game> createGame(@RequestBody Game game) throws IllegalArgumentException {
        Game createdLobby = gameService.createGame(game);
        return new ResponseEntity<>(createdLobby, HttpStatus.CREATED);
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

    @PostMapping("{gameId}/start")
    @ResponseStatus(HttpStatus.OK)
    public Game startGame(@PathVariable("gameId") Integer gameId) {
        Game game = gameService.findGameById(gameId);
        if (game.getPlayers().size() < 2) {
            throw new IllegalArgumentException("Not enough players to start the game");
        }
        return gameService.startGame(gameId);
    }

    /**
     * This endpoint will be called from the user's browser to join a game lobby. If the response is 200 OK (The game
     * hasn't started yet and there is still space), then he/she will be redirected to the lobby
     * 
     * @param lobbyId id of the lobby that the user wants to join to
     * @param token JWT token of the user, to validate server-side that the user who wants to join is the one who is
     *        logged in
     * @return 200 OK if the game hasn't started yet and there is still space
     * @return 404 NOT FOUND if the lobby doesn't exist
     * @return 403 FORBIDDEN if the game has already started, the players is already in the game or there is no room
     *         left
     * @return 401 UNAUTHORIZED if the user who sent the request is not logged or is not the one who he/she claims to be
     *         (JWT token validation)
     * @author andrsdt
     */
    @PostMapping("{gameId}/add/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Game joinGame(@PathVariable("gameId") Integer gameId, @PathVariable("username") String username,
            @RequestHeader("Authorization") String token) {
        Game game = gameService.joinGame(gameId, username, token);
        log.info("User " + username + " joined game with id " + gameId);
        return game;
    }

    @PostMapping("{gameId}/remove/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Game removeFromGame(@PathVariable("gameId") Integer gameId, @PathVariable("username") String username,
            @RequestHeader("Authorization") String token) {
        Game game = gameService.removePlayer(gameId, username, token);
        log.info("User " + username + " was removed from game " + gameId);
        return game;
    }

    /**
     * This endpoint will receive the petitions of a player to play a card
     * 
     * @author andrsdt
     * @param entity with the information of the card to play
     * @return the game with the updated state
     */
    @PostMapping("{gameId}/ability-cards/{abilityCardIngameId}")
    public ResponseEntity<Game> playCard(@PathVariable("gameId") Integer gameId,
            @PathVariable("abilityCardIngameId") Integer abilityCardIngameId, @RequestBody Map<String, Integer> body,
            @RequestHeader("Authorization") String token) {
        Integer enemyId = body.get("enemyId");
        gameService.playCard(abilityCardIngameId, enemyId, token);
        Game game = gameService.findGameById(gameId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping("{gameId}/market-cards/{marketCardIngameId}")
    public ResponseEntity<Game> buyMarketCard(@PathVariable("gameId") Integer gameId,
            @PathVariable("marketCardIngameId") Integer marketCardIngameId,
            @RequestHeader("Authorization") String token) {
        gameService.buyMarketCard(marketCardIngameId, token);
        Game game = gameService.findGameById(gameId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping("{gameId}/turn")
    public ResponseEntity<Turn> getTurn(@PathVariable("gameId") Integer gameId) {
        Turn turn = gameService.getCurrentTurnByGameId(gameId);
        return new ResponseEntity<>(turn, HttpStatus.OK);
    }

    @PostMapping("{gameId}/turn/next")
    public ResponseEntity<Game> nextTurn(@PathVariable("gameId") Integer gameId) {
        Turn turn = gameService.getCurrentTurnByGameId(gameId);
        gameService.setNextTurnState(turn);
        Game game = gameService.findGameById(gameId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
