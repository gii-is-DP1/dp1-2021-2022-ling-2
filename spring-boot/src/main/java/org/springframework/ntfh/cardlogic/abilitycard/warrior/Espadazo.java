package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 1
 * Roba 1 carta si es el primer "Espadazo" que juegas este turno.
 * 
 * @author Pablosancval
 */
@Component
public class Espadazo {

    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(1, playerFrom, targetedEnemy).execute();

        if (!targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.ESPADAZO)) {
            new DrawCommand(1, playerFrom).execute();
        }

        playerFrom.getGame().getEnemiesFighting()
                .forEach(x -> x.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.ESPADAZO));

    }
}
