package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GoldOnKillCommand;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class AtaqueFurtivo{
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DealDamageCommand(2, targetedEnemy).execute();

        AbilityCardIngame cardIngame = new AbilityCardIngame();
        cardIngame.setId(48);
        cardIngame.setPlayer(playerFrom);

        AbilityCard card=new AbilityCard();
        card.setAbilityCardTypeEnum(AbilityCardTypeEnum.ATAQUE_FURTIVO);
        card.setCharacterTypeEnum(CharacterTypeEnum.ROGUE);
    
        cardIngame.setAbilityCard(card);

        if(!targetedEnemy.getPlayedCardsOnMeInTurn().contains(cardIngame)){
            new GoldOnKillCommand(1, targetedEnemy, playerFrom).execute();
        }

        targetedEnemy.getPlayedCardsOnMeInTurn().add(cardIngame);
    }
    
}
