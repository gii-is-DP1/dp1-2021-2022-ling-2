package org.springframework.ntfh.entity.game.history;

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
// TODO replace this and all its references in the frontend/backend with
// "/game-history" (API naming conventions)
public class GameHistoryController {

    @Autowired
    private GameHistoryService gameHistoryService;

    @GetMapping
    public ResponseEntity<Iterable<GameHistory>> getAll() {
        Iterable<GameHistory> gameHistory = gameHistoryService.findAll();
        return new ResponseEntity<>(gameHistory, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount() {
        Integer gameHistoryCount = gameHistoryService.count();
        return new ResponseEntity<>(gameHistoryCount, HttpStatus.OK);
    }
}
