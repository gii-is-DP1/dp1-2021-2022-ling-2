package org.springframework.ntfh.entity.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/players")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PutMapping("{playerId}/character/{characterId}")
    @ResponseStatus(HttpStatus.OK)
    public void setCharacter(@PathVariable("playerId") Integer playerId,
            @PathVariable("characterId") Integer characterId, @RequestHeader("Authorization") String token) {
        // TODO use converters for this
        playerService.updateCharacter(playerId, characterId);
    }
}


