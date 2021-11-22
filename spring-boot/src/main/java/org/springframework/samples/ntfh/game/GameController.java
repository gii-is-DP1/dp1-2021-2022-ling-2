package org.springframework.samples.ntfh.game;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * This endpoint handles the creation of a new game lobby
     * 
     * @param game data introduced by the game creator in the form
     * @return id of the game so the user can be redirected to the lobby
     * @author andrsdt
     */
    @PostMapping("new")
    public ResponseEntity<Map<String, Integer>> createGame(@RequestBody Game game) {
        // TODO untested
        Game createdGame = gameService.save(game);
        return new ResponseEntity<>(Map.of("gameId", createdGame.getId()), HttpStatus.CREATED);
    }
}
