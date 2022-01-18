package org.springframework.ntfh.entity.spectator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/spectators")
public class SpectatorController {

    @Autowired
    private SpectatorService spectatorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Spectator> getAll() {
        Iterable<Spectator> spectators = this.spectatorService.findAll();
        return spectators;
    }

}
