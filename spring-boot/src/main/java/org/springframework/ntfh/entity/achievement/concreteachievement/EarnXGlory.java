package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.user.User;

public class EarnXGlory implements Achievement {

    public Boolean check(User user, Integer numRequested) {
        Integer earnedGlory = user.getPlayers().stream().mapToInt(Player::getGlory).sum();
        return earnedGlory >= numRequested;
    }
}
