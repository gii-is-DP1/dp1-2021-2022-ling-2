package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GiveWoundCommand implements Command {

    private Player playerTo;

    @Override
    public void execute() {
        Integer currentWounds = playerTo.getWounds();
        playerTo.setWounds(currentWounds + 1);
        if (playerTo.isDead()) {
            // TODO implement death
        }
    }

}
