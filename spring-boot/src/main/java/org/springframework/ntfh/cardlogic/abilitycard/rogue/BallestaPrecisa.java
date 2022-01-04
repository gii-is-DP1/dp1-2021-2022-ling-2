package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Si ya utilizaste una "Ballesta precisa" contra este mismo enemigo, el daño de
 * esta carta es 3.
 * 
 * @author andrsdt
 */
@Component
public class BallestaPrecisa {

    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        
        if(targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.BALLESTA_PRECISA)){
            new DealDamageCommand(3, targetedEnemy).execute();
        }else{
            new DealDamageCommand(2, targetedEnemy).execute();
        }

        targetedEnemy.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.BALLESTA_PRECISA);
    }
}
