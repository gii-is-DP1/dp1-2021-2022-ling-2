package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.GiveGloryCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 4
 * Gana 1 ficha de Gloria. Pierdes 1 carta.
 * 
 * @author Pablosancval
 */
@Component
public class EnLaDiana {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(4, playerFrom, targetedEnemy).execute();
        new GiveGloryCommand(1, playerFrom).execute();
        new DiscardCommand(1, playerFrom).execute();
    }
}
