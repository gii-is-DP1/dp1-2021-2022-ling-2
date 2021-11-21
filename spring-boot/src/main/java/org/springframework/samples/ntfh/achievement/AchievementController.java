package org.springframework.samples.ntfh.achievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping
    public ResponseEntity<Iterable<Achievement>> getAll() {
        // untested
        Iterable<Achievement> achievements = this.achievementService.findAll();
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

}
