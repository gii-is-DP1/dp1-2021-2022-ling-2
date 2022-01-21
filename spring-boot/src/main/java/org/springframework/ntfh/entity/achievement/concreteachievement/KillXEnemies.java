package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.user.User;

public class KillXEnemies implements Achievement {

    public Boolean check(User user, Integer numRequested) {
        Integer killedEnemies = user.getPlayers().stream().mapToInt(Player::getKills).sum();
        return killedEnemies >= numRequested;
    }
}
