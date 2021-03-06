package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 1
 * Aumenta en 1 el daño de las siguientes cartas que causen Daño a este enemigo.
 * Pierdes 1 carta.
 * 
 * @author Pablosancval
 */
@Component
public class FlechaCorrosiva {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(1, playerFrom, targetedEnemy).execute();
        new DiscardCommand(1, playerFrom).execute();
        targetedEnemy.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.FLECHA_CORROSIVA);
    }

}
