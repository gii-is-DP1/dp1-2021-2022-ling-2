package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.player.Player;

public class Reconstitucion {
    public void execute(Player playerFrom){
        new DrawCommand(1, playerFrom).execute();
        new RecoverCommand(2, playerFrom).execute();
    }
}
