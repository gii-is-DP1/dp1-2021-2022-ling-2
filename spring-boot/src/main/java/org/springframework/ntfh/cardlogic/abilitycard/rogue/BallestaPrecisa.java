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
 * Si ya utilizaste una "Ballesta precisa" contra este mismo enemigo, el daño de
 * esta carta es 3.
 * 
 * @author andrsdt
 */
@Component
public class BallestaPrecisa {

    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        
        AbilityCardIngame cardIngame = new AbilityCardIngame();
        cardIngame.setId(51);
        cardIngame.setPlayer(playerFrom);

        AbilityCard card=new AbilityCard();
        card.setAbilityCardTypeEnum(AbilityCardTypeEnum.BALLESTA_PRECISA);
        card.setCharacterTypeEnum(CharacterTypeEnum.ROGUE);
    
        cardIngame.setAbilityCard(card);

        if(targetedEnemy.getPlayedCardsOnMeInTurn().contains(cardIngame)){
            new DealDamageCommand(3, targetedEnemy).execute();
        }else{
            new DealDamageCommand(2, targetedEnemy).execute();
        }

        targetedEnemy.getPlayedCardsOnMeInTurn().add(cardIngame);
    }
}
