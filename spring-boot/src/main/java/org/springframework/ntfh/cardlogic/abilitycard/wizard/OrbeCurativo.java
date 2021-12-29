package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.List;

import org.springframework.ntfh.command.HealCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class OrbeCurativo {
    public void execute(Player playerFrom, Game game){
        new HealCommand(playerFrom);
        List<Player> targets = game.getPlayers();
        for(Player playerTarget:targets){
            new RecoverCommand(2, playerTarget).execute();
        }
        //TODO still dont know how to make the exhaust (exile) command, had a couple ideas but must confirm with other members
    }
}
