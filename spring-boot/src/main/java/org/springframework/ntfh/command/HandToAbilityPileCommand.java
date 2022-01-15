package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HandToAbilityPileCommand implements Command {

    private Player playerFrom;
    private AbilityCardTypeEnum cardToReturn;

    @Override
    public void execute() {
        List<AbilityCardIngame> inHand = playerFrom.getHand();
        List<AbilityCardIngame> abilityPile = playerFrom.getAbilityPile();
        for (AbilityCardIngame card : inHand) {
            AbilityCardTypeEnum cardEnum = card.getAbilityCardTypeEnum();
            if (cardEnum == cardToReturn) {
                abilityPile.add(card);
                playerFrom.setAbilityPile(abilityPile);
                inHand.remove(card);
                playerFrom.setHand(inHand);
                break;
            }
        }
    }
}
