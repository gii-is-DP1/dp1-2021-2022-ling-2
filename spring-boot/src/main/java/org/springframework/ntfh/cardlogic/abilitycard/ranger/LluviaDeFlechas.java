package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import java.util.List;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;

public class LluviaDeFlechas {
    public void execute(Player playerFrom, Game game){
        List<EnemyIngame> targetList = game.getEnemiesFighting();
        for(EnemyIngame target:targetList){
            new DealDamageCommand(2, target).execute();
        }
        //TODO daño al aliado con menos heridas en caso de empate elección
    }
}
