package org.springframework.ntfh.entity.statistics;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrsdt
 */
@RestController
@RequestMapping("/stats")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("ranking/wins")
    @ResponseStatus(HttpStatus.OK)
    public List<Pair<String, Integer>> getRankingByWins() {
        return statisticsService.rankingByWonGames();
    }

    @GetMapping("ranking/glory")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> getRankingByglory() {
        // return statisticsService.getRankingByGlory();
        // Game longest = statisticsService.longestGame();
        // Game shortest = statisticsService.shortestGame();
        // Map<String, Integer> fiveMoreKills = statisticsService.rankingByTotalKills();
        // Map<String, Integer> fiveMoreGlory = statisticsService.rankingByTotalGlory();
        // Double averageNumPlayers = statisticsService.averagePlayersPerGame();
        return null;
    }

    @GetMapping("ranking/kills")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> getRankingBykills() {
        // return statisticsService.getRankingByKills();
        return null;
    }
}
