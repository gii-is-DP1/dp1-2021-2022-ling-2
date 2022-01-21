package org.springframework.ntfh.entity.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ntfh.entity.statistic.pojo.GlobalStats;
import org.springframework.ntfh.entity.statistic.pojo.UserStats;
import org.springframework.ntfh.entity.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

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
        return UserStats.builder() // Start building the POJO
                .matchesPlayed(statisticsService.getNumGamesPlayedUser(user))
                .matchesWon(statisticsService.getNumVictoriesByUser(user))
                .fastestMatch(statisticsService.getMinTimePlayedUser(user))
                .longestMatch(statisticsService.getMaxTimePlayedUser(user))
                .averageDuration(statisticsService.getAvgDurationOfGames(user))
                .charactersPlayed(statisticsService.getListPlayedCharactersByUser(user))
                .charactersWinRates(statisticsService.getListWinRatioByCharacter(user))
                .killCount(statisticsService.getKillsUser(user))
                .gloryEarned(statisticsService.getUserTotalGloryPoints(user)) //
                .build(); // Build and return
    }

    /**
     * Get a custom object with statistics to show in a player's profile
     * 
     * @author andrsdt
     * @param user
     * @return
     **/
    @GetMapping("global")
    @ResponseStatus(HttpStatus.OK)
    public GlobalStats getGlobalStats() {
        return GlobalStats.builder() // Start building the POJO
                .matchesPlayed(statisticsService.countFinishedGames())
                .averageGamesPerUser(statisticsService.getAvgGamesPlayedPerUser())
                .totalGameHours(statisticsService.getTotalGameHours())
                .rankingByWins(statisticsService.getRankingByWins())
                .rankingByGlory(statisticsService.getRankingByGlory())
                .rankingByKills(statisticsService.getRankingByKills()) //
                .totalUsers(statisticsService.countUsers()) //
                .build(); // Build and return
    }

    @GetMapping("users/{userId}/games/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer gamesPlayedByUser(@PathVariable("userId") User user) {
        return statisticsService.countFinishedByUser(user);
    }

    @GetMapping("games/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer countFinishedGames() {
        return statisticsService.countFinishedGames();
    }
}
