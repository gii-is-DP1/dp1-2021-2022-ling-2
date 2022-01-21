package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class WinXGames implements Achievement {

    public Boolean check(User user, Integer numRequested) {
        // TODO implement with query?
        Long numWonGames = user.getPlayers().stream().filter(p -> p.getGame().getWinner().equals(p)).count();
        return numWonGames >= numRequested;
    }
}
