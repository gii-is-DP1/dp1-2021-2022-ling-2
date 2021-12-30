package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class RestrainCommand implements Command{

    private EnemyIngame targetedEnemy;

    @Override
    public void execute() {
        targetedEnemy.setRestrained(true);
    }
}
