package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GiveGuardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Previenes 2 de Daño.
 * 
 * @author Pablosancval
 */
@Component
public class CargaConEscudo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(2, playerFrom, targetedEnemy).execute();
        new GiveGuardCommand(2, playerFrom).execute();
    }
}
