package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import java.util.List;

import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.GetGloryCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class VozDeAliento {
    public void execute(Player playerFrom, Game game){
        new DrawCommand(1, playerFrom).execute();
        new GetGloryCommand(1, playerFrom).execute();
        List<Player> players = game.getPlayers();
        for(Player playerTarget:players){
            new RecoverCommand(2, playerTarget).execute();
        }
    }
}
