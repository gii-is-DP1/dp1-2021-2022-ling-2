package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DealDamageAOECommand implements Command {

    private Integer damage;
    private List<EnemyIngame> EnemiesFighting;

    @Override
    public void execute() {
        Integer i=0;
        Integer currentEndurance = 0; 
        for(i=0; i<EnemiesFighting.size(); i++)
            currentEndurance =EnemiesFighting.get(i).getCurrentEndurance();
            EnemiesFighting.get(i).setCurrentEndurance(currentEndurance - damage);
    }
}
