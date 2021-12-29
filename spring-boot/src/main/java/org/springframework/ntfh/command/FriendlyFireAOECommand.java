package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.command.DiscardCommand;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FriendlyFireAOECommand implements Command{
    
    private Integer damage;
    private List<Player> players;
    private Player playerFrom;
    
    @Override
    public void execute() {
        Integer i = 0;
        List<Player> targets = players;
        targets.remove(playerFrom);
        for(i=0; i<targets.size(); i++){
            new DiscardCommand(damage, targets.get(i)).execute();

        }
        
        
    }
    
}
