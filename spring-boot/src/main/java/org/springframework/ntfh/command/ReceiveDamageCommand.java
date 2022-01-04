package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReceiveDamageCommand implements Command {

    private Integer damage;

    private Player playerTo;

    @Override
    public void execute() {
        for (int i = 0; i < damage; i++) {
            Boolean noCardsLeft = playerTo.getAbilityPile().isEmpty();
            if (noCardsLeft) {
                new GiveWoundCommand(playerTo);
                break;
            }
            Integer topCardIndex = playerTo.getAbilityPile().size() - 1;
            AbilityCardIngame topCard = playerTo.getAbilityPile().get(topCardIndex);
            playerTo.getAbilityPile().remove(topCard);
            playerTo.getDiscardPile().add(topCard);
        }
    }

}
