package org.springframework.ntfh.cardlogic.abilitycard.warrior;


import java.util.stream.Collectors;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;



@Component
public class Espadazo {

    
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DealDamageCommand(1, targetedEnemy).execute();
        
        AbilityCardIngame cardIngame = new AbilityCardIngame();
        cardIngame.setId(36);
        cardIngame.setPlayer(playerFrom);

        AbilityCard card=new AbilityCard();
        card.setAbilityCardTypeEnum(AbilityCardTypeEnum.GOLPE_DE_BASTON);
        card.setCharacterTypeEnum(CharacterTypeEnum.WIZARD);
    
        cardIngame.setAbilityCard(card);
        
        
        Long instances= targetedEnemy.getPlayedCardsOnMeInTurn().stream().filter(x -> x.equals(cardIngame)).collect(Collectors.counting());
        
        if(instances < 1){
            new DrawCommand(1, playerFrom).execute();
        }
        
        targetedEnemy.getPlayedCardsOnMeInTurn().add(cardIngame);
        //TODO condición de repetición
    }
}
