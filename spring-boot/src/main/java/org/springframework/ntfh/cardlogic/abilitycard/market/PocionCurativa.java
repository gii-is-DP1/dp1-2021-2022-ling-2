package org.springframework.ntfh.cardlogic.abilitycard.market;

import org.springframework.ntfh.command.HealCommand;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class PocionCurativa {
    public void execute(Player playerFrom){
        new HealCommand(playerFrom).execute();
        //TODO Exile/exhaust
    }
}
