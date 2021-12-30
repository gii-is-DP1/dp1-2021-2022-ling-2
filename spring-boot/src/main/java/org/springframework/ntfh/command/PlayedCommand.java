package org.springframework.ntfh.command;

import java.util.Iterator;
import java.util.List;

import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.playablecard.abilitycard.*;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayedCommand implements Command{
    
    private Player playerFrom;
    private AbilityCardIngame cardPlayed;

    @Override
    public void execute() {
        List<AbilityCardIngame> hand =  playerFrom.getHand();
        List<AbilityCardIngame> discardPile = playerFrom.getDiscardPile();
        AbilityCardTypeEnum cardPlayedEnum = cardPlayed.getAbilityCardTypeEnum();
        for(AbilityCardIngame card:hand){
            AbilityCardTypeEnum carta = card.getAbilityCardTypeEnum();
            if(carta.equals(cardPlayedEnum)){
                discardPile.add(cardPlayed);
                playerFrom.setDiscardPile(discardPile);
                hand.remove(cardPlayed);
                break;
            }
        }
    }
}
