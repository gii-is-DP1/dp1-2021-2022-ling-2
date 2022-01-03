package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class PasoAtras {
    public void execute(Player playerFrom){
        new DrawCommand(2, playerFrom).execute();
    }
}
