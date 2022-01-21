package org.springframework.ntfh.entity.statistic;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.game.GameRepository;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    // *******For Global Statistics********

    public Integer countFinishedGames() {
        return gameRepository.countByStateType(GameStateType.FINISHED);
    }

    public Integer countUsers() {
        return (int) userRepository.count();
    }

    public Double getAvgGamesPlayedPerUser() {
        Long numberOfUsers = userRepository.count();
        return countFinishedGames() / numberOfUsers.doubleValue();
    }

    public Integer getTotalGameHours() {
        return statisticsRepository.globalDurationOfGames() / 1000 / 3600;
    }

    public List<User> getUsersOrderedByNumberOfGames(Pageable pageable) {
        return statisticsRepository.listUserByNumberOfGames(pageable);
    }

    public List<Pair<String, Long>> getRankingByWins() {
        Pageable topFive = PageRequest.of(0, 5);
        return statisticsRepository.rankingByWins(topFive).stream()
                .map(list -> Pair.of((String) list.get(0), (long) list.get(1)))
                .collect(java.util.stream.Collectors.toList());
    }


    public List<Pair<String, Long>> getRankingByGlory() {
        Pageable topFive = PageRequest.of(0, 5);
        return statisticsRepository.rankingByGlory(topFive).stream()
                .map(list -> Pair.of((String) list.get(0), (long) list.get(1)))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Pair<String, Long>> getRankingByKills() {
        Pageable topFive = PageRequest.of(0, 5);
        return statisticsRepository.rankingByKills(topFive).stream()
                .map(list -> Pair.of((String) list.get(0), (long) list.get(1)))
                .collect(java.util.stream.Collectors.toList());
    }

    // ******User Statistics********

    // Number of games played by the user
    public int countFinishedByUser(User user) {
        return statisticsRepository.countByUser(user);
    }

    public Timestamp getAvgDurationOfGames(User user) {
        Double milliseconds = statisticsRepository.avgUserDurationOfGames(user);
        if (milliseconds == null)
            return null;
        return new Timestamp(milliseconds.longValue());
    }

    public Timestamp getMinTimePlayedUser(User user) {
        Integer milliseconds = statisticsRepository.minDurationOfGames(user);
        if (milliseconds == null)
            return null;
        return new Timestamp(milliseconds.longValue());
    }

    public Timestamp getMaxTimePlayedUser(User user) {
        Integer milliseconds = statisticsRepository.maxDurationOfGames(user);
        if (milliseconds == null)
            return null;
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

    public Map<CharacterTypeEnum, Double> getListWinRatioByCharacter(User user) {
        Map<CharacterTypeEnum, Double> res = new EnumMap<>(CharacterTypeEnum.class);
        Arrays.asList(CharacterTypeEnum.values()).forEach(type -> res.put(type, getWinRatioCharacter(user, type)));
        return res;
    }

    public Double getWinRatioCharacter(User user, CharacterTypeEnum character) {
        Integer wonGames = getNumGamesWonWithCharacter(user, character);
        Integer totalGames = getNumGamesPlayedWithCharacter(user, character);
        return (double) wonGames / totalGames;
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

    public Iterable<Statistics> findByUser(User user) {
        return statisticsRepository.findByuser(user);
    }
}
