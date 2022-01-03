package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetGuardCommand implements Command{

    private Integer guard;
    private Player playerFrom;

    @Override
    public void execute() {
        if(playerFrom.getGuard()<guard){
            playerFrom.setGuard(guard);
        }
    }
    
}
