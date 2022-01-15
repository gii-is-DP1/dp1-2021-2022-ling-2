package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GiveGuardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 1
 * Previenes 2 puntos de Daño.
 * 
 * @author Pablosancval
 */
@Component
public class EnLasSombras {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(1, playerFrom, targetedEnemy).execute();
        new GiveGuardCommand(2, playerFrom).execute();
    }
}
