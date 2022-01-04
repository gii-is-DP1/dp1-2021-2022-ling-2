package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiscardCommand implements Command {

    private Integer numDiscards;

    private Player playerFrom;

    @Override
    public void execute() {
        for (int i = 0; i < numDiscards; i++) {
            List<AbilityCardIngame> listAbilityPile = playerFrom.getAbilityPile();
            if (listAbilityPile.size() >= 1) {
                AbilityCardIngame discardedCard = listAbilityPile.get(0);
                listAbilityPile.remove(0);
                List<AbilityCardIngame> listDiscardPile = playerFrom.getDiscardPile();
                listDiscardPile.add(discardedCard);

                playerFrom.setAbilityPile(listAbilityPile);
                playerFrom.setDiscardPile(listDiscardPile);
            } else {
                break;
            }
        }
    }
}
