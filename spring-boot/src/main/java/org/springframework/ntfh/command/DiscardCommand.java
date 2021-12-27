package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiscardCommand implements Command{
    
    private Integer numDiscards;
    private Player playerFrom;

    @Override
    public void execute() {
        
    }
}
