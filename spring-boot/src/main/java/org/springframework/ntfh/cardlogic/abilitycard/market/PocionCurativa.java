package org.springframework.ntfh.cardlogic.abilitycard.market;

import org.springframework.ntfh.command.ExileCommand;
import org.springframework.ntfh.command.HealCommand;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class PocionCurativa {
    public void execute(Player playerFrom, AbilityCardIngame cardPlayed){
        new HealCommand(playerFrom).execute();
        new ExileCommand(playerFrom, cardPlayed).execute();
    }
}
