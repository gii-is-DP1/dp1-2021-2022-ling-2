package org.springframework.ntfh.command;


import org.springframework.ntfh.entity.enemy.hordeenemy.ingame.HordeEnemyIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GoldOnKillCommand implements Command{

    private Integer gold;
    private HordeEnemyIngame targetedEnemy;
    private Player playerFrom;

    @Override
    public void execute() {
        if(targetedEnemy.getCurrentEndurance()<=0){
            Integer currentGold = playerFrom.getGold();
            playerFrom.setGold(currentGold+gold);
        }
        
    }
}
