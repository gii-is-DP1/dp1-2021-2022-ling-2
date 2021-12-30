package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
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

    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed) {
        // TODO missing the damage increment
        new DealDamageCommand(2, targetedEnemy).execute();
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
