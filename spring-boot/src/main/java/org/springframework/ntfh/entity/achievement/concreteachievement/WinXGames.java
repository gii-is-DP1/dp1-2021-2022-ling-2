package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.statistics.StatisticsService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class WinXGames {

    @Autowired
    private StatisticsService statisticsService;

    public Boolean check(User userFrom, Integer numberOfGames) {
        Integer wonGames = statisticsService.countFinishedByUser(userFrom);
        return wonGames >= numberOfGames;
    }
}
