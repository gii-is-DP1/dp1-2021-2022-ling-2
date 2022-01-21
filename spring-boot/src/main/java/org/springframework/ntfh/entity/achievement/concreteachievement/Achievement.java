package org.springframework.ntfh.entity.achievement.concreteachievement;

import org.springframework.ntfh.entity.user.User;

public interface Achievement {
    public Boolean check(User user, Integer numRequested);
}
