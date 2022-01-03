package org.springframework.ntfh.command;

import java.util.Collections;
import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecoverQSCommand implements Command{
    
    private Player playerFrom;

    @Override
    public void execute() {
        AbilityCardIngame SearchedCard = null;
        List<AbilityCardIngame> listDiscardPile = playerFrom.getDiscardPile();
        List<AbilityCardIngame> listAbilityPile = playerFrom.getAbilityPile();
        if(listDiscardPile.contains(SearchedCard)){
            int position = listDiscardPile.indexOf(SearchedCard);
            AbilityCardIngame toBeReturned = listDiscardPile.get(position);
            listDiscardPile.remove(position);
            playerFrom.setDiscardPile(listDiscardPile);
            listAbilityPile.add(toBeReturned);
            Collections.shuffle(listAbilityPile);
            playerFrom.setAbilityPile(listAbilityPile);
        }
    }    
}
