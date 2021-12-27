package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.hordeenemy.ingame.HordeEnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Si ya utilizaste una "Ballesta precisa" contra este mismo enemigo, el daño de
 * esta carta es 3.
 * 
 * @author andrsdt
 */
@Component
public class BallestaPrecisa {

    public void execute(Player playerFrom, HordeEnemyIngame targetedEnemy) {
        // TODO missing the complex condition
        new DealDamageCommand(3, targetedEnemy);
    }
}
