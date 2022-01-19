package org.springframework.ntfh.entity.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.GameRepository;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    GameRepository gameRepository;

    public int countFinishedByUser(User user) {
        return gameRepository.countFinishedByUser(user, GameStateType.FINISHED);
    }
}
