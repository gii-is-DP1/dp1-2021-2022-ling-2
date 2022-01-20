package org.springframework.ntfh.entity.achievement;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/achievements")
public class AchievementController {
    @Autowired
    private AchievementService achievementService;

    @GetMapping
    public ResponseEntity<Iterable<Achievement>> getAll() {
        Iterable<Achievement> achievements = this.achievementService.findAll();
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

    @GetMapping("{achievementId}")
    public ResponseEntity<Achievement> getAchivementById(@PathVariable("achievementId") Integer id) {
        Optional<Achievement> achievement = this.achievementService.findAchievementById(id);
        if (!achievement.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(achievement.get(), HttpStatus.OK);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateAchievement(@RequestBody Achievement achievements, @RequestHeader("Authorization") String token) {
        achievementService.updateAchievement(achievements, token);
    }

}
