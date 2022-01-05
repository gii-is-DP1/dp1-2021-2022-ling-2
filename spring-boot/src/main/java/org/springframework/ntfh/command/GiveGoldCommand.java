package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GiveGoldCommand implements Command {

    private Integer gold;

    private Player playerFrom;

    @Override
    public void execute() {
        Integer currentGold = playerFrom.getGold();
        playerFrom.setGold(currentGold + gold);
    }
}
