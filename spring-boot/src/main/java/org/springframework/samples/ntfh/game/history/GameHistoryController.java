package org.springframework.samples.ntfh.game.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/gameHistory")
public class GameHistoryController {
    
    @Autowired
    private GameHistoryService gameHistoryService;

    @GetMapping
    public ResponseEntity<Iterable<GameHistory>> getAll() {
        Iterable<GameHistory> gameHistory = gameHistoryService.findAll();
        return new ResponseEntity<>(gameHistory, HttpStatus.OK);
    }
}
