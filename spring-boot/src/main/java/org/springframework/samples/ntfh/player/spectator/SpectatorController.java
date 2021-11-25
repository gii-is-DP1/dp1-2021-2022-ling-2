package org.springframework.samples.ntfh.player.spectator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO change to rest controller and return JSON
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SpectatorController {

    @Autowired
    private SpectatorService spectatorService;

    @GetMapping
    public ResponseEntity<Iterable<Spectator>> getAll() {
        // untested
        Iterable<Spectator> spectators = this.spectatorService.findAll();
        return new ResponseEntity<>(spectators, HttpStatus.OK);
    }

}
