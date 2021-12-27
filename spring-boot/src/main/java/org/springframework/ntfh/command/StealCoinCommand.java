package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

public class StealCoinCommand implements Command {

    private Player playerFrom;

    private Player playerTo;

    public StealCoinCommand(Player playerFrom, Player playerTo) {
        this.playerFrom = playerFrom;
        this.playerTo = playerTo;
    }

    @Override
    public void execute() {
        // Remove a coin from the player who is being robbed
        if (playerTo.getGold() == 0)
            return; // If the player has no coins, do nothing
        playerTo.setGold(playerTo.getGold() - 1);
        // And give it to the player who is robbing
        playerFrom.setGold(playerFrom.getGold() + 1);
    }
}
