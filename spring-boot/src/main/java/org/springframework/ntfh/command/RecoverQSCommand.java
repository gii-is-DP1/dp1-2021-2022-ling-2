package org.springframework.ntfh.command;

import java.util.Collections;
import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecoverQSCommand implements Command {

    private Player playerFrom;

    @Override
    public void execute() {
        AbilityCardIngame searchedCard = null;
        List<AbilityCardIngame> listDiscardPile = playerFrom.getDiscardPile();
        List<AbilityCardIngame> listAbilityPile = playerFrom.getAbilityPile();
        if (listDiscardPile.contains(searchedCard)) {
            int position = listDiscardPile.indexOf(searchedCard);
            AbilityCardIngame toBeReturned = listDiscardPile.get(position);
            listDiscardPile.remove(position);
            playerFrom.setDiscardPile(listDiscardPile);
            listAbilityPile.add(toBeReturned);
            Collections.shuffle(listAbilityPile);
            playerFrom.setAbilityPile(listAbilityPile);
        }
    }
}
