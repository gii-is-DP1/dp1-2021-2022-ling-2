package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.user.User;

public class PlayXDistinctCharacters implements Achievement {

    @Override
    public Boolean check(User user, Integer numRequested) {
        // TODO implement with query?
        Long distinctPlayersPlayedByUser =
                user.getPlayers().stream().map(Player::getCharacterTypeEnum).distinct().count();
        return distinctPlayersPlayedByUser >= numRequested;
    }

}
