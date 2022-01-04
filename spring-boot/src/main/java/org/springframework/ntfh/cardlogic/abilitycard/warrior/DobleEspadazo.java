package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Pierdes 1 carta
 * 
 * @author Pablosancval
 */
@Component
public class DobleEspadazo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(2, targetedEnemy).execute();
        new DiscardCommand(1, playerFrom).execute();
    }
}
