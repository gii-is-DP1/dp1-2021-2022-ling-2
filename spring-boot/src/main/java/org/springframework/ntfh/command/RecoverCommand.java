package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecoverCommand implements Command {

    private Player playerFrom;

    @Override
    public void execute() {
        List<AbilityCardIngame> discardPile = playerFrom.getDiscardPile();
        if (discardPile.isEmpty())
            return;
        AbilityCardIngame recoveredCard = discardPile.get(0);
        discardPile.remove(recoveredCard);
        playerFrom.getAbilityPile().add(recoveredCard);
    }
}
