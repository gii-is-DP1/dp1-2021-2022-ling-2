package org.springframework.ntfh.entity.statistic.pojo;

import java.util.List;
import org.springframework.data.util.Pair;
import lombok.Builder;

@Builder
public class GlobalStats {
    public Integer totalUsers;

    public Integer matchesPlayed;

    public Double averageGamesPerUser;

    public Integer totalGameHours;

    public List<Pair<String, Long>> rankingByWins;

    public List<Pair<String, Long>> rankingByGlory;

    public List<Pair<String, Long>> rankingByKills;
}
