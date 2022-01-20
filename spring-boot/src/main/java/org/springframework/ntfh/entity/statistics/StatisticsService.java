package org.springframework.ntfh.entity.statistics;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.entity.game.Game;
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

    public Game longestGame() {
        Pageable firstResult = PageRequest.of(0, 1);
        return gameRepository.gameWithMaxDuration(GameStateType.FINISHED, firstResult).get(0);
    }

    public Game shortestGame() {
        Pageable firstResult = PageRequest.of(0, 1);
        return gameRepository.gameWithMinDuration(GameStateType.FINISHED, firstResult).get(0);
    }

    public Integer totalGameDuration() {
        // TODO: implement
        return null;
    }

    public Integer averageGameDuration() {
        // TODO: implement
        return null;
    }

    public Double averagePlayersPerGame() {
        // ! NOT WORKING
        // return gameRepository.averagePlayersPerGame();
        return null;
    }

    public Integer averageTurnsPerGame() {
        // TODO: implement
        // Personal idea
        return null;
    }

    public List<User> rankingByWonGames() {
        Pageable topFive = PageRequest.of(0, 5);
        return userRepository.rankingByWonGames(topFive);
    }

    public List<Object> rankingByTotalGlory() {
        Pageable topFive = PageRequest.of(0, 5);
        return userRepository.rankingByTotalGlory(topFive);
    }

    public List<Object> rankingByTotalKills() {
        Pageable topFive = PageRequest.of(0, 5);
        return userRepository.rankingByTotalKills(topFive);
    }
}
