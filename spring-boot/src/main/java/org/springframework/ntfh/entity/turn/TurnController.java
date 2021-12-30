package org.springframework.ntfh.entity.turn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/turns")
public class TurnController {

    @Autowired
    private TurnService turnService;

    @Autowired
    private GameService gameService;

    @PostMapping("/button")
    public ResponseEntity<Turn> pressButton(@RequestBody Turn turn) {
        turnService.stateButton(turn);
        return new ResponseEntity<>(turn, HttpStatus.OK);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Turn> getTurn(@PathVariable("gameId") Integer gameId) {
        Turn turn = gameService.getCurrentTurnByGameId(gameId);
        return new ResponseEntity<>(turn, HttpStatus.OK);
    }
}
