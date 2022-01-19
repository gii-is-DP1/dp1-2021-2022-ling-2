package org.springframework.ntfh.cardlogic.abilitycard.market;

import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Modificador: Spell, Ranged
 * El enemigo seleccionado no causa daño este turno.
 * 
 * @author Pablosancval
 */
@Component
public class CapaElfica {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new RestrainCommand(targetedEnemy).execute();
    }
}
