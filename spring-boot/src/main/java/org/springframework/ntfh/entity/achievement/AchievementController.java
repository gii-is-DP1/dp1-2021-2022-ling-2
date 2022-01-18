package org.springframework.ntfh.entity.achievement;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/achievements")
public class AchievementController {
    @Autowired
    private AchievementService achievementService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Achievement> getAll() {
        Iterable<Achievement> achievements = this.achievementService.findAll();
        return achievements;
    }


    // ! TODO a mirar como gestionar el notfound para poder aplicar el nuevo standard, seguramente sea un throw exception

    @GetMapping("{achievementId}")
    public ResponseEntity<Achievement> getAchivementById(@PathVariable("achievementId") Integer id) {
        Optional<Achievement> achievement = this.achievementService.findAchievementById(id);
        if (!achievement.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(achievement.get(), HttpStatus.OK);
    }


    // ! TODO a mirar como cambiar este putmapping, creemos que le falta devolver algo para la nueva forma de hacer la respuesta
    @PutMapping()
	@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> updateAchievement(@RequestBody @Valid Achievement achievements,
            @RequestHeader("Authorization") String token) {
        achievementService.updateAchievement(achievements, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
