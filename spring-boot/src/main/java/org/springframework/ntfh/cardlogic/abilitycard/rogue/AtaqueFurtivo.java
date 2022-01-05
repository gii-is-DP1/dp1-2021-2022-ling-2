package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GoldOnKillCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Si derrotas un enemigo con este ataque, ganas 1 Moneda si no has jugado otro
 * "Ataque furtivo" este turno.
 * 
 * @author Pablosancval
 */
@Component
public class AtaqueFurtivo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(2, targetedEnemy).execute();

        if(!targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.ATAQUE_FURTIVO)){
            new GoldOnKillCommand(1, targetedEnemy, playerFrom).execute();
        }

        targetedEnemy.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.ATAQUE_FURTIVO);
    }

}
