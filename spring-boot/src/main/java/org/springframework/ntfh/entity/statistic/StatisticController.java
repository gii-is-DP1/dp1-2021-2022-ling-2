package org.springframework.ntfh.entity.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ntfh.entity.statistic.pojo.UserStats;
import org.springframework.ntfh.entity.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticController {

    @Autowired
    StatisticService statisticsService;

    /**
     * Get a custom object with statistics to show in a player's profile
     * 
     * @author pabrobcam
     * @param user
     * @return
     **/
    @GetMapping("users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserStats getUserStats(@PathVariable("userId") User user) {
        UserStats userstats = new UserStats();

        userstats.matchesPlayed = statisticsService.getNumGamesPlayedUser(user);
        userstats.matchesWon = statisticsService.getNumVictoriesByUser(user);
        userstats.fastestMatch = statisticsService.getMinTimePlayedUser(user);
        userstats.longestMatch = statisticsService.getMaxTimePlayedUser(user);
        userstats.averageDuration = statisticsService.getAvgDurationOfGames(user);
        userstats.killCount = statisticsService.getKillsUser(user);
        userstats.gloryEarned = statisticsService.getUserTotalGloryPoints(user);
        userstats.charactersPlayed = statisticsService.getListPlayedCharactersByUser(user);

        return userstats;
    }

    @GetMapping("users/{userId}/games/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer gamesPlayedByUser(@PathVariable("userId") User user) {
        return statisticsService.countFinishedByUser(user);
    }
}
