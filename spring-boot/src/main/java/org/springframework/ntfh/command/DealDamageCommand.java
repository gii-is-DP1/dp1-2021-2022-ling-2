package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DealDamageCommand implements Command {

    private Integer damage;

    private EnemyIngame targetedEnemy;

    @Override
    public void execute() {
        Integer currentEndurance = targetedEnemy.getCurrentEndurance();
        targetedEnemy.setCurrentEndurance(currentEndurance - damage);

        Boolean dead = (currentEndurance - damage <= 0);
        if (dead) {
            targetedEnemy.setCurrentEndurance(0);
            targetedEnemy.getGame().getEnemiesFighting().remove(targetedEnemy);
        }
    }
}
