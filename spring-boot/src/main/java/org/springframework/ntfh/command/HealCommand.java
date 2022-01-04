package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HealCommand implements Command {

    private Player playerFrom;

    @Override
    public void execute() {
        if (playerFrom.getWounds() >= 1) {
            playerFrom.setWounds(playerFrom.getWounds() - 1);
        }
    }
}
