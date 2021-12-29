package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor
public class RecoverAOECommand implements Command{

    private Integer amount;
    private List<Player> players;
    
    @Override
    public void execute() {
        Integer i = 0;
        for(i=0; i<players.size(); i++){
            new RecoverCommand(amount, players.get(i)).execute();
        }
        
    }
    
    
}
