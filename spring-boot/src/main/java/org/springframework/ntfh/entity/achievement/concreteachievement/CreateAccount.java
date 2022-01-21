package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class CreateAccount implements Achievement {

    public Boolean check(User user, Integer numRequested) {
        return user != null; // If the user exists, you have created an account
    }
}
