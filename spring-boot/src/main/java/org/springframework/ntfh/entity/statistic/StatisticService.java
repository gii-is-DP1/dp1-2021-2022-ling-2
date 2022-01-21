package org.springframework.ntfh.entity.statistic;

import java.sql.Timestamp;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.game.GameRepository;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    @Autowired
    private StatisticRepository statisticsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    // *******For Global Statistics********
    @Transactional
    public Integer getTotalNumberOfGames() {
        return statisticsRepository.numberOfGamesTotal();
    }

    public List<User> getListUsersByNumberOfGames(Pageable pageable) {
        return statisticsRepository.listUserByNumberOfGames(pageable);
    }

    public Double getAvgGamesPlayed() {
        Iterator<User> it = userRepository.findAll().iterator();
        Integer userCounter = 0;
        while (it.hasNext()) {
            userCounter++;
            it.next();
        }
        Double res = (double) (getTotalNumberOfGames() / userCounter);
        return res;
    }

    public Integer getTotalPlayedTime() {
        return statisticsRepository.globalDurationOfGames();
    }


    // ******User Statistics********

    // Number of games played by the user
    public int countFinishedByUser(User user) {
        return gameRepository.countFinishedByUser(user, GameStateType.FINISHED);
    }

    public Timestamp getAvgDurationOfGames(User user) {
        Double milliseconds = statisticsRepository.avgUserDurationOfGames(user);
        return new Timestamp(milliseconds.longValue());
    }

    public Timestamp getMinTimePlayedUser(User user) {
        Integer milliseconds = statisticsRepository.minDurationOfGames(user);
        return new Timestamp(milliseconds.longValue());
    }

    public Timestamp getMaxTimePlayedUser(User user) {
        Integer milliseconds = statisticsRepository.maxDurationOfGames(user);
        return new Timestamp(milliseconds.longValue());
    }

    // Number of games and Wins
    public Integer getNumGamesPlayedUser(User user) {
        return statisticsRepository.findNumberOfGamesByUser(user);
    }

    public Integer getNumVictoriesByUser(User user) {
        return statisticsRepository.findNumberOfVictoriesByUser(user);
    }

    public Double getWinRatio(User user) {
        return (double) getNumVictoriesByUser(user) / getNumGamesPlayedUser(user);
    }

    // Characters
    public Map<CharacterTypeEnum, Long> getListPlayedCharactersByUser(User user) {
        List<List<Object>> listOfLists = statisticsRepository.listCharactersPlayedByUser(user);
        Map<CharacterTypeEnum, Long> res = new EnumMap<>(CharacterTypeEnum.class);
        listOfLists.forEach(pair -> res.put((CharacterTypeEnum) pair.get(0), (Long) pair.get(1)));
        return res;
    }

    public Integer getNumGamesPlayedWithCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findNumberOfGamesWithCharacter(user, character);
    }

    public Integer getNumGamesWonWithCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findNumberOfWinsWithCharacter(user, character);
    }

    public Double getWinRatioCharacter(User user, CharacterTypeEnum character) {
        return (double) getNumGamesWonWithCharacter(user, character) / getNumGamesPlayedWithCharacter(user, character);
    }


    // Deaths
    public Integer getDeathsbyUser(User user) {
        return statisticsRepository.findNumberOfDeathsbyUser(user);
    }

    public Integer getDeathsWithCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findNumberOfDeathsbyUser(user);
    }

    public Double getDeathRatioWithCharacter(User user, CharacterTypeEnum character) {
        return (double) getDeathsWithCharacter(user, character) / getDeathsbyUser(user);
    }

    public Double getDeathRatio(User user) {
        Integer numOfGamesByUser = gameRepository.findFinishedByUser(user, GameStateType.FINISHED, Pageable.unpaged())
                .getNumberOfElements();
        return (double) getDeathsbyUser(user) / numOfGamesByUser;
    }

    // Kills
    public Integer getKillsUser(User user) {
        return statisticsRepository.findTotalNumberOfkillsByUser(user);
    }

    public Integer getMaxKillsInOneGame(User user) {
        return statisticsRepository.findMaxNumberOfkillsByUser(user);
    }


    public Double getAvgKillsUser(User user) {
        return statisticsRepository.findAVGNumberOfkillsByUser(user);
    }


    public Integer getKillsUserWithCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findMaxNumberOfkillsByUserWithCharacter(user, character);
    }

    public Integer getMaxKillsUserWithCharacterInOneGame(User user, CharacterTypeEnum character) {
        return statisticsRepository.findMaxNumberOfkillsByUserWithCharacter(user, character);
    }

    public Double getAvgKillsUserWithCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findAVGNumberOfkillsByUserWithCharacter(user, character);
    }

    // Glory
    public Integer getUserTotalGloryPoints(User user) {
        return statisticsRepository.findTotalGloryPointsByUser(user);
    }

    public Integer getMaxUserGloryPointsEarned(User user) {
        return statisticsRepository.findMaxGloryPointsByUser(user);
    }

    public Double getAvgGloryPointsEarned(User user) {
        return statisticsRepository.findAVGGloryPointsByUser(user);
    }

    public Integer getTotalUserGloryPointsCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findTotalGloryPointsByUserWithCharacter(user, character);
    }

    public Integer getMaxUserGloryPointsCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findMaxGloryPointsByUserWithCharacter(user, character);
    }

    public Double getAvgUserGloryPointsCharacter(User user, CharacterTypeEnum character) {
        return statisticsRepository.findAVGGloryPointsByUserWithCharacter(user, character);
    }


    // Other services
    public Statistics save(Statistics statistic) {
        return statisticsRepository.save(statistic);
    }
}
