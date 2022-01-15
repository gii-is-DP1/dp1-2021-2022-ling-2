package org.springframework.ntfh.command;

import java.util.Collections;
import java.util.stream.IntStream;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GiveWoundCommand implements Command {

    private Player playerTo;

    @Override
    public void execute() {
        // Recover all the cards
        Integer discardPileSize = playerTo.getDiscardPile().size();
        IntStream.range(0, discardPileSize).forEach(i -> new RecoverCommand(playerTo).execute());

        // Shuffle the new abilityPile
        Collections.shuffle(playerTo.getAbilityPile());

        // Give the player a wound
        playerTo.setWounds(playerTo.getWounds() + 1);
    }

}
