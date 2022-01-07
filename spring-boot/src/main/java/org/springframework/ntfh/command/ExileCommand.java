package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExileCommand implements Command {

    private Player playerFrom;

    private AbilityCardTypeEnum cardPlayed;

    @Override
    public void execute() {
        List<AbilityCardIngame> hand = playerFrom.getHand();
        for (AbilityCardIngame card : hand) {
            AbilityCardTypeEnum carta = card.getAbilityCardTypeEnum();
            if (carta.equals(cardPlayed)) {
                hand.remove(card);
                break;
            }
        }
    }
}
