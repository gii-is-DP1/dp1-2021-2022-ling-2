package org.springframework.ntfh.entity.statistic.metaStatistic;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.http.HttpStatus;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.statistic.metaStatistic.POJO.UserStats;
import org.springframework.ntfh.entity.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/statistics")
public class MetaStatisticController {
    

    MetaStatisticService metaStatisticService;


    @GetMapping("user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserStats getUserStats(@PathVariable("userId")User user){
        UserStats userstats= new UserStats();

        userstats.matchesPlayed=metaStatisticService.getNumGamesPlayedUser(user);
        userstats.matchesWon=metaStatisticService.getNumVictoriesByUser(user);
        userstats.fastestMatch=metaStatisticService.getMinTimePlayedUser(user);
        userstats.longestMatch=metaStatisticService.getMaxTimePlayedUser(user);
        userstats.averageDuration=metaStatisticService.getAvgDurationOfGames(user);
        userstats.killCount=metaStatisticService.getKillsUser(user);
        userstats.gloryEarned=metaStatisticService.getUserTotalGloryPoints(user);


        List<Object> aux=metaStatisticService.getListPlayedCharactersByUser(user);
        SortedMap<String,Integer> characterPlayed= new TreeMap<String,Integer>();
        
        for(int i=0;i<aux.size();i=i+2){
            Object chracter = aux.get(i);
            Object numOfGames= aux.get(i+1);
            characterPlayed.put(chracter.toString(), Integer.valueOf(numOfGames.toString()));
        }

        userstats.charactersPlayed=characterPlayed;

        SortedMap<String,Double> winPerCharacter = new TreeMap<String,Double>();
        SortedMap<String, Integer> deathsCharacter = new TreeMap<String,Integer>();
        
        Iterator<String> characters= characterPlayed.keySet().iterator();
        
        while(characters.hasNext()){
            String character=characters.next();

            Double winRatio=metaStatisticService.getWinRatioCharacter(user, CharacterTypeEnum.valueOf(character));
            Integer deaths=metaStatisticService.getKillsUserWithCharacter(user, CharacterTypeEnum.valueOf(character));
            
            winPerCharacter.put(character, winRatio);
            deathsCharacter.put(character, deaths);
        }

        return userstats;

    }




}
