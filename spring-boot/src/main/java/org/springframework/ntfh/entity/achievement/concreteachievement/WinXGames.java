package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class WinXGames implements Achievement {

    public Boolean check(User user, Integer numRequested) {
        Long numWonGames = user.getPlayers().stream().filter(p -> p.equals(p.getGame().getWinner())).count();
        return numWonGames >= numRequested;
    }
}
