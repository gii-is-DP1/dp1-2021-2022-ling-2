package org.springframework.ntfh.command;

import java.util.Collections;
import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecoverQSCommand implements Command {

    private Player playerFrom;
    private AbilityCardTypeEnum searchedCardEnum;

    @Override
    public void execute() {
        List<AbilityCardIngame> listDiscardPile = playerFrom.getDiscardPile();
        List<AbilityCardIngame> listAbilityPile = playerFrom.getAbilityPile();
        for(AbilityCardIngame card : listDiscardPile){
            if (card.getAbilityCardTypeEnum().equals(searchedCardEnum)){
                int position = listDiscardPile.indexOf(card);
                AbilityCardIngame toBeReturned = listDiscardPile.get(position);
                listDiscardPile.remove(position);
                playerFrom.setDiscardPile(listDiscardPile);
                listAbilityPile.add(toBeReturned);
                Collections.shuffle(listAbilityPile);
                playerFrom.setAbilityPile(listAbilityPile);
            }
        }
    }
}
