package org.springframework.samples.ntfh.game;

import java.io.StringReader;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping()
    public ResponseEntity<Iterable<GameEntity>> getAll() {
        // TODO untested
        Iterable<GameEntity> games = gameService.findAll();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @PostMapping("new")
    public ResponseEntity<Map<String, String>> createGame(@Valid @RequestBody GameEntity game) {
        // TODO untested
        gameService.save(game);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
