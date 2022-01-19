package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GiveGloryCommand implements Command {

    private Integer glory;
    private Player playerFrom;

    @Override
    public void execute() {
        Integer currentGlory = playerFrom.getGlory();
        if(glory>=0 || currentGlory>=Math.abs(glory)){
            playerFrom.setGlory(currentGlory + glory);
        } else {
            playerFrom.setGlory(0);
        }
    }
}
