package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.GoldOnKillCommand;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 4
 * Si derrotas un enemigo con este atauqe, ganas 1 Moneda si no has jugado otro
 * "Al corazón" este turno. Pierdes 1 carta.
 * 
 * @author Pablosancval
 */
@Component
public class AlCorazon {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(4, targetedEnemy).execute();


        if(!targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.AL_CORAZON)){
            new GoldOnKillCommand(1, targetedEnemy, playerFrom).execute();
        }

        targetedEnemy.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.AL_CORAZON);
        new DiscardCommand(1, playerFrom).execute();
    }
}
