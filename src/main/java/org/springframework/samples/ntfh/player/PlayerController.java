package org.springframework.samples.ntfh.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

// TODO change to rest controller and return JSON
@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public ResponseEntity<Iterable<Player>> getAll() {
        // untested
        Iterable<Player> players = this.playerService.findAll();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

}
