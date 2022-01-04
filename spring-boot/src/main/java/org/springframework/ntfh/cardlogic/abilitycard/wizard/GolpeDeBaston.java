package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 1
 * Si ya utilizaste "Golpe de bastón" contra el mismo enemigo, el daño de esta
 * carta es 2.
 * 
 * @author Pablosancval
 */
@Component
public class GolpeDeBaston {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        

        AbilityCardIngame cardIngame = new AbilityCardIngame();
        cardIngame.setId(36);
        cardIngame.setPlayer(playerFrom);

        AbilityCard card=new AbilityCard();
        card.setAbilityCardTypeEnum(AbilityCardTypeEnum.GOLPE_DE_BASTON);
        card.setCharacterTypeEnum(CharacterTypeEnum.WIZARD);
    
        cardIngame.setAbilityCard(card);

        if(targetedEnemy.getPlayedCardsOnMeInTurn().contains(cardIngame)){
            new DealDamageCommand(2, targetedEnemy).execute();
        }else{
            new DealDamageCommand(1, targetedEnemy).execute();
        }

        targetedEnemy.getPlayedCardsOnMeInTurn().add(cardIngame);
    }
}
