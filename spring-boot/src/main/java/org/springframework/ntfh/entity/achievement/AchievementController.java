package org.springframework.ntfh.entity.achievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/achievements")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Achievement> getPage(@PageableDefault(page = 0, size = 5) final Pageable pageable) {
        return achievementService.findPageable(pageable);
    }

    @GetMapping("types")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<AchievementType> getTypes() {
        return this.achievementService.findAllTypes();
    }

    @GetMapping("count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getAchievementCount() {
        return this.achievementService.count();
    }

    @GetMapping("{achievementId}")
    @ResponseStatus(HttpStatus.OK)
    public Achievement getAchivementById(@PathVariable("achievementId") Integer id) {
        return this.achievementService.findById(id);
    }

    @PostMapping("new")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAchievement(@RequestBody Achievement achievement) {
        achievementService.createAchievement(achievement);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateAchievement(@RequestBody Achievement achievement, @RequestHeader("Authorization") String token) {
        achievementService.updateAchievement(achievement, token);
    }

    @DeleteMapping("{achievementId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAchievement(@PathVariable("achievementId") Achievement achievement) {
        achievementService.delete(achievement);
    }
}
