package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DrawCommand implements Command {

    private Integer amount;

    private Player playerFrom;

    @Override
    public void execute() {
        for (int i = 0; i < amount; i++) {
            List<AbilityCardIngame> listAbilityPile = playerFrom.getAbilityPile();
            if (!listAbilityPile.isEmpty()) {
                AbilityCardIngame drawnCard = listAbilityPile.get(0);

                listAbilityPile.remove(drawnCard);
                if (listAbilityPile.isEmpty())
                    new GiveWoundCommand(playerFrom).execute();

                playerFrom.getHand().add(drawnCard);
            } else {
                break;
            }
        }
    }

}
