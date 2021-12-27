package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.GetGloryCommand;
import org.springframework.ntfh.command.GetGoldCommand;
import org.springframework.ntfh.entity.player.Player;

public class SaqueoOroGloria {
    public void execute(Player playerFrom){
        Integer nEnemies = 0;
        //TODO enemies on table
        new GetGoldCommand(nEnemies, playerFrom).execute();
        new GetGloryCommand(nEnemies, playerFrom).execute();
    }
}
