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
        if(gold>=0 || currentGold>=Math.abs(gold)){
            playerFrom.setGold(currentGold + gold);
        } else {
            playerFrom.setGold(0);
        }
    }
}
