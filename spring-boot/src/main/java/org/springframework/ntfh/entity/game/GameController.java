package org.springframework.ntfh.entity.game;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrsdt
 */
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
        // TODO untested
        Iterable<Game> games = gameService.findAll();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    /**
     * This endpoint handles the creation of a new game from a lobby
     * 
     * @param lobby object with the preferences for the game
     * @return id of the game so the user can be redirected from the lobby
     * @author andrsdt
     */
    @PostMapping
    public ResponseEntity<Map<String, Integer>> createGame(@RequestBody Lobby lobby) {
        Game createdGame = gameService.createFromLobby(lobby);
        return new ResponseEntity<>(Map.of("gameId", createdGame.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount() {
        Integer count = gameService.gameCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable("gameId") Integer gameId) {
        Game game = gameService.findGameById(gameId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    /**
     * This endpoint will receive the petitions of a player to play a card
     * 
     * @author andrsdt
     * @param entity with the information of the card to play
     * @return the game with the updated state
     */
    @PostMapping("/{gameId}/ability-cards/{abilityCardIngameId}")
    public ResponseEntity<Game> playCard(@PathVariable("gameId") Integer gameId,
            @PathVariable("abilityCardIngameId") Integer abilityCardIngameId, @RequestBody Map<String, Integer> body) {
        Integer enemyId = body.get("enemyId");
        gameService.playCard(abilityCardIngameId, enemyId);
        Game game = gameService.findGameById(gameId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/market-cards/{marketCardIngameId}")
    public ResponseEntity<Game> buyMarketCard(@PathVariable("gameId") Integer gameId,
            @PathVariable("marketCardIngameId") Integer marketCardIngameId,
            @RequestHeader("Authorization") String token) {
        gameService.buyMarketCard(marketCardIngameId, token);
        Game game = gameService.findGameById(gameId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
