package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GetGloryCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class ProyectilIgneo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DealDamageCommand(2, targetedEnemy).execute();
        new GetGloryCommand(1, playerFrom);
    }
}
