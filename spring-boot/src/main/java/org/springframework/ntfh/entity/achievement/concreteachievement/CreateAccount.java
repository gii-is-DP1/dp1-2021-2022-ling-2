package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class CreateAccount {

    public Boolean check(User userFrom, Integer numberOfGames) {
        return true;
    }
}
