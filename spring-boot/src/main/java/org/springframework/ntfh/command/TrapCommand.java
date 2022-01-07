package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrapCommand implements Command{
    
    private Player playerFrom;
    private EnemyIngame targetedEnemy;
    
    @Override
    public void execute() {
        Integer gloryToGain = targetedEnemy.getEnemy().getBaseGlory();
        Integer currentGlory = playerFrom.getGlory();
        targetedEnemy.getGame().getEnemiesFighting().remove(targetedEnemy);
        playerFrom.setGlory(currentGlory+gloryToGain);
    }
}
