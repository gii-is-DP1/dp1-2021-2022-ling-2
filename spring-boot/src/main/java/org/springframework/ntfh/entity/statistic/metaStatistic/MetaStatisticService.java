package org.springframework.ntfh.entity.statistic.metaStatistic;

import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;

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
public class MetaStatisticService {
    

    private MetaStatisticRepository metaStatisticRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    public MetaStatisticService(MetaStatisticRepository metaStatisticRepository){
        this.metaStatisticRepository=metaStatisticRepository;
    }

    @Transactional
    public Integer getTotalNumberOfGames(){
        return metaStatisticRepository.numberOfGamesTotal();
    }

    public List<User> getListUsersByNumberOfGames(Pageable pageable){
        return metaStatisticRepository.listUserByNumberOfGames(pageable);
    }

    public Double getAvgGamesPlayed(){
        Iterator<User> it= userRepository.findAll().iterator();
        Integer userCounter=0;
        while(it.hasNext()){
            userCounter++;
            it.next();
        }
        Double res= (double) (getTotalNumberOfGames()/userCounter);
        return res;
    }


    public Integer getTotalPlayedTime(){
        return metaStatisticRepository.globalDurationOfGames();
    }

    public Double getAvgDurationOfGames(User user){
        return metaStatisticRepository.AvgUserDurationOfGames(user);
    }

    public Integer getMinTimePlayedUser(User user){
        return metaStatisticRepository.MinDurationOfGames(user);
    }

    public Integer getMaxTimePlayedUser(User user){
        return metaStatisticRepository.MaxDurationOfGames(user);
    }



    public Integer getNumGamesPlayedUser(User user){
        return metaStatisticRepository.findNumberOfGamesByUser(user);
    }

    public Integer getNumVictoriesByUser(User user){
        return metaStatisticRepository.findNumberOfVictoriesByUser(user);
    }

    public Double getWinRatio(User user){
        return (double) getNumVictoriesByUser(user)/getNumGamesPlayedUser(user);
    }

    public List<Object> getListPlayedCharactersByUser(User user){
        return metaStatisticRepository.listCharactersPlayedByUser(user);
    }

    public Integer getNumGamesPlayedWithCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findNumberOfGamesWithCharacter(user, character);
    }

    public Integer getNumGamesWonWithCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findNumberOfWinsWithCharacter(user, character);
    }

    public Double getWinRatioCharacter(User user, CharacterTypeEnum character){
        return (double) getNumGamesWonWithCharacter(user, character)/getNumGamesPlayedWithCharacter(user, character);
    }
    
    public Integer getDeathsbyUser(User user){
        return metaStatisticRepository.findNumberOfDeathsbyUser(user);
    }

    public Integer getDeathsWithCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findNumberOfDeathsbyUser(user);
    }

    public Double getDeathRatioWithCharacter(User user, CharacterTypeEnum character){
        return (double) getDeathsWithCharacter(user, character)/getDeathsbyUser(user);
    }

    public Double getDeathRatio(User user){
        Integer numOfGamesByUser=gameRepository.findFinishedByUser(user, GameStateType.FINISHED).size();
        return (double) getDeathsbyUser(user)/numOfGamesByUser;
    }

    public Integer getKillsUser(User user){
        return metaStatisticRepository.findTotalNumberOfkillsByUser(user);
    }

    public Integer getMaxKillsInOneGame(User user){
        return metaStatisticRepository.findMaxNumberOfkillsByUser(user);
    }

    public Integer getMinKillsInOneGame(User user){
        return metaStatisticRepository.findMinNumberOfkillsByUser(user);
    }

    public Double getAvgKillsUser(User user){
        return metaStatisticRepository.findAVGNumberOfkillsByUser(user);
    }


    public Integer getKillsUserWithCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findMaxNumberOfkillsByUserWithCharacter(user, character);
    }

    public Integer getMaxKillsUserWithCharacterInOneGame(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findMaxNumberOfkillsByUserWithCharacter(user, character);
    }

    public Integer getMinKillsUserWithCharacterInOneGame(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findMinNumberOfkillsByUserWithCharacter(user, character);
    }

    public Double getAvgKillsUserWithCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findAVGNumberOfkillsByUserWithCharacter(user, character);
    }

    public Integer getUserTotalGloryPoints(User user){
        return metaStatisticRepository.findTotalGloryPointsByUser(user);
    }

    public Integer getMaxUserGloryPointsEarned(User user){
        return metaStatisticRepository.findMaxGloryPointsByUser(user);
    }

    public Integer getMinUserGloryPointsEarned(User user){
        return metaStatisticRepository.findMinGloryPointsByUser(user);
    }


    public Double getAvgGloryPointsEarned(User user){
        return metaStatisticRepository.findAVGGloryPointsByUser(user);
    }

    public Integer getTotalUserGloryPointsCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findTotalGloryPointsByUserWithCharacter(user, character);
    }

    public Integer getMaxUserGloryPointsCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findMaxGloryPointsByUserWithCharacter(user, character);
    }

    public Integer getMinUserGloryPoitnsCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findMinGloryPointsByUserWithCharacter(user, character);
    }

    public Double getAvgUserGloryPointsCharacter(User user, CharacterTypeEnum character){
        return metaStatisticRepository.findAVGGloryPointsByUserWithCharacter(user, character);
    }
}
