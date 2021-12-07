package org.springframework.ntfh.entity.enemy.warlord.ingame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrsdt
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/warlords")
public class WarlordIngameController {

    @Autowired
    private WarlordIngameService warlordIngameService;

    @GetMapping("/{gameId}")
    public ResponseEntity<WarlordIngame> getWarlordFromGame(@PathVariable("gameId") Integer gameId) {
        WarlordIngame warlordIngame = warlordIngameService.findWarlordByGameId(gameId);
        return new ResponseEntity<>(warlordIngame, HttpStatus.OK);
    }
}
