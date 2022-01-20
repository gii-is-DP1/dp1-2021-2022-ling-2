package org.springframework.ntfh.entity.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.entity.game.GameRepository;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    public int countFinishedByUser(User user) {
        return gameRepository.countFinishedByUser(user, GameStateType.FINISHED);
    }

    public User maxNumberOfGamesPlayed() {
        Pageable firstResult = PageRequest.of(0, 1);
        return userRepository.maxNumberOfGamesPlayed(firstResult).get(0);
    }

    public User maxNumberOfGamesWon() {
        Pageable firstResult = PageRequest.of(0, 1);
        return userRepository.maxNumberOfGamesWon(firstResult).get(0);
    }
}
