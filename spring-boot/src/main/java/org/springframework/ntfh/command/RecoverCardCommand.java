package org.springframework.ntfh.command;

import java.util.Collections;
import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecoverCardCommand implements Command {

    private Player playerFrom;
    private AbilityCardTypeEnum searchedCardEnum;

    @Override
    public void execute() {
        List<AbilityCardIngame> discardPile = playerFrom.getDiscardPile();
        List<AbilityCardIngame> abilityPile = playerFrom.getAbilityPile();

        AbilityCardIngame sameTypeCard = discardPile.stream()
                .filter(card -> card.getAbilityCardTypeEnum().equals(searchedCardEnum))
                .findAny()
                .orElse(null);

        discardPile.remove(sameTypeCard);
        abilityPile.add(sameTypeCard);

        Collections.shuffle(abilityPile);
    }
}
