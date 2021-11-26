package org.springframework.samples.ntfh.enemy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/horde_enemies") // TODO follow Rest API naming conventions
public class HordeEnemyController {

    @Autowired
    private HordeEnemyService hordeEnemyService;

    @GetMapping()
    public ResponseEntity<Iterable<HordeEnemy>> getAll() {
        // TODO untested
        // We don't know if this method will be useful later
        Iterable<HordeEnemy> hordeEnemies = hordeEnemyService.findAll();
        return new ResponseEntity<>(hordeEnemies, HttpStatus.OK);
    }

}
