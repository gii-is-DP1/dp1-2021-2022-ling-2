package org.springframework.ntfh.entity.statistic.metaStatistic.POJO;

import java.util.SortedMap;

public class UserStats {
    public Integer matchesPlayed;
    public Integer matchesWon;
    public Integer fastestMatch;
    public Integer longestMatch;
    public Double averageDuration;
    public SortedMap<String,Integer> charactersPlayed; 
    public SortedMap <String, Double> charactersWinRates;
    public SortedMap <String, Integer> characterDeaths;
    public Integer killCount;
    public Integer gloryEarned;
}
