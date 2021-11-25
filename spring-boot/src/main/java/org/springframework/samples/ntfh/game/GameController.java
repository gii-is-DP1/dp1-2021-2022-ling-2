package org.springframework.samples.ntfh.game;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.ntfh.lobby.Lobby;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        // TODO untested
        Game createdGame = gameService.createFromLobby(lobby);
        return new ResponseEntity<>(Map.of("gameId", createdGame.getId()), HttpStatus.CREATED);
    }
}
