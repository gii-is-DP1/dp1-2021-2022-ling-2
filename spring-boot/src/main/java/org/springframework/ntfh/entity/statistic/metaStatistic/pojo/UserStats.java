package org.springframework.ntfh.entity.statistic.metaStatistic.pojo;

import java.sql.Timestamp;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;

public class UserStats {
    // POJO for parsing to JSON
    public Integer matchesPlayed;

    public Integer matchesWon;

    @JsonFormat(pattern = "HH:mm:ss")
    public Timestamp fastestMatch;

    @JsonFormat(pattern = "HH:mm:ss")
    public Timestamp longestMatch;

    @JsonFormat(pattern = "HH:mm:ss")
    public Timestamp averageDuration;

    public Map<CharacterTypeEnum, Long> charactersPlayed;

    public Map<CharacterTypeEnum, Double> charactersWinRates;

    public Map<CharacterTypeEnum, Long> characterDeaths;

    public Integer killCount;

    public Integer gloryEarned;
}
