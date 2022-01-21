package org.springframework.ntfh.entity.statistic.metaStatistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ntfh.entity.statistic.metaStatistic.pojo.UserStats;
import org.springframework.ntfh.entity.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/statistics")
public class MetaStatisticController {

    @Autowired
    MetaStatisticService metaStatisticService;

    @GetMapping("users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserStats getUserStats(@PathVariable("userId") User user) {
        UserStats userstats = new UserStats();

        userstats.matchesPlayed = metaStatisticService.getNumGamesPlayedUser(user);
        userstats.matchesWon = metaStatisticService.getNumVictoriesByUser(user);
        userstats.fastestMatch = metaStatisticService.getMinTimePlayedUser(user);
        userstats.longestMatch = metaStatisticService.getMaxTimePlayedUser(user);
        userstats.averageDuration = metaStatisticService.getAvgDurationOfGames(user);
        userstats.killCount = metaStatisticService.getKillsUser(user);
        userstats.gloryEarned = metaStatisticService.getUserTotalGloryPoints(user);
        userstats.charactersPlayed = metaStatisticService.getListPlayedCharactersByUser(user);

        return userstats;
    }
}
