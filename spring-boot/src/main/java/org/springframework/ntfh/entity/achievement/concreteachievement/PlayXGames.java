package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class PlayXGames implements Achievement {

    public Boolean check(User user, Integer numRequested) {
        Integer playedGames = user.getPlayers().size();
        return playedGames >= numRequested;
    }
}
