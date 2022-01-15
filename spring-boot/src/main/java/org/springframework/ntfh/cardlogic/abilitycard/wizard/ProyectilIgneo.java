package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GiveGloryCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Ganas 1 ficha de Gloria.
 * 
 * @author Pablosancval
 */
@Component
public class ProyectilIgneo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(2, playerFrom, targetedEnemy).execute();
        new GiveGloryCommand(1, playerFrom).execute();
    }
}
