package org.springframework.ntfh.enemy.ingame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/horde-enemies")
public class HordeEnemyIngameController {

    @Autowired
    private HordeEnemyIngameService hordeEnemyIngameService;

    @GetMapping("/{gameId}")
    public ResponseEntity<Iterable<HordeEnemyIngame>> getHordeEnemiesFromGame(@PathVariable("gameId") Integer gameId) {
        Iterable<HordeEnemyIngame> hordeEnemiesIngame = hordeEnemyIngameService.findHordeEnemyByGameId(gameId);
        return new ResponseEntity<>(hordeEnemiesIngame, HttpStatus.OK);
    }
}
