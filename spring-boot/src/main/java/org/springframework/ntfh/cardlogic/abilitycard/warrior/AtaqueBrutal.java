package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 3
 * Pierdes 1 carta
 * 
 * @author Pablosancval
 */
@Component
public class AtaqueBrutal {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(3, playerFrom, targetedEnemy).execute();
        new DiscardCommand(1, playerFrom).execute();
    }
}
