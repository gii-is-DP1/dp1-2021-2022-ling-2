package org.springframework.samples.ntfh.achievement;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping
    public String getAll(ModelMap modelMap) {
        String view = "achievements/listAchievements";
        Iterable<Achievement> achievements = achievementService.findAll();
        modelMap.addAttribute("achievements", achievements);
        return view;
    }

    @GetMapping(path = "/new")
    public String createAchievement(ModelMap modelMap) {
        String view = "achievements/editAchievement";
        modelMap.addAttribute("achievement", new Achievement()); // we add a new created achievement to the context so
                                                                 // we can edit it
        return view;
    }

    @PostMapping(path = "/save")
    public String saveAchievement(@Valid Achievement achievement, BindingResult result, ModelMap modelMap) {
        String view = "achievements/listAchievements";
        if (result.hasErrors()) {
            modelMap.addAttribute("achievement", achievement); // achievement with errors as new context to represent it
                                                               // again
            return "achievements/editAchievement"; // and we return to the form which should show the errors
        } else {
            achievementService.save(achievement);
            modelMap.addAttribute("message", "Achievement successfully saved");
            view = getAll(modelMap); // after saving the achievement, we will udpate the view with the new data
        }
        return view;
    }

    @GetMapping(path = "/delete/{achievementId}")
    public String deleteAchievement(@PathVariable("achievementId") Integer achievementId, ModelMap modelMap) {
        String view = "achievements/listAchievements";
        Optional<Achievement> achievement = achievementService.findAchievementById(achievementId);
        if (achievement.isPresent()) {
            achievementService.delete(achievementId);
            modelMap.addAttribute("message", "Achievement deleted");
            view = getAll(modelMap); // after saving the achievement, we will udpate the view with the new data
        } else {
            modelMap.addAttribute("message", "Achievement not found");
        }
        return view;
    }

}
