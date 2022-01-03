package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnedToAbilityPileCommand implements Command{
    
    private Player playerFrom;
    private AbilityCardIngame cardPlayed;
    
    @Override
    public void execute() {
        List<AbilityCardIngame> inHand = playerFrom.getHand();
        List<AbilityCardIngame> abilityPile = playerFrom.getAbilityPile();
        AbilityCardTypeEnum cardPlayedEnum = cardPlayed.getAbilityCardTypeEnum();
        for(AbilityCardIngame card:inHand){
            AbilityCardTypeEnum cardEnum = card.getAbilityCardTypeEnum();
            if(cardEnum.equals(cardPlayedEnum)){
                abilityPile.add(cardPlayed);
                playerFrom.setAbilityPile(abilityPile);
                inHand.remove(cardPlayed);
                break;
            }
        }

        
    }
    
}
