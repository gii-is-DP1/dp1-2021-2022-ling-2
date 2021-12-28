package org.springframework.ntfh.cardEfects.commands;

import org.springframework.ntfh.cardEfects.Command;
import org.springframework.ntfh.cardEfects.OperationsReceiver;
import org.springframework.ntfh.entity.player.Player;

public class GiveGlory implements Command {
    
    private Player player;//Player that is playing the card 
    private Integer gloryValue;
    private OperationsReceiver operationsReceiver;

    public void GiveGloryCommand(OperationsReceiver operationsReceiver){
        this.operationsReceiver=operationsReceiver;
    }

    @Override
    public void execute() {
        operationsReceiver.giveGlory(player, gloryValue);
    
    }
}
