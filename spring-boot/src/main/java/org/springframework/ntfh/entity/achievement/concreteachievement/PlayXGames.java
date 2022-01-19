package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class PlayXGames {

    public Boolean check(User userFrom, Integer numberOfGames) {
        Integer playedGames = userFrom.getPlayers().size();
        return playedGames >= numberOfGames;
    }
}
