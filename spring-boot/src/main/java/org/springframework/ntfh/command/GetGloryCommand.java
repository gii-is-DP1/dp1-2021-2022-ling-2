package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetGloryCommand implements Command {

    private Integer glory;

    private Player playerFrom;

    @Override
    public void execute() {
        Integer currentGlory = playerFrom.getGlory();
        playerFrom.setGlory(currentGlory + glory);
    }
}
