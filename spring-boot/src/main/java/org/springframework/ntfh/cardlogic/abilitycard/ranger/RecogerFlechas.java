package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.GetGoldCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.command.RecoverQSCommand;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

public class RecogerFlechas {
    public void execute(Player playerFrom, AbilityCardIngame cardPlayed){
        new GetGoldCommand(1, playerFrom).execute();
        new RecoverQSCommand(playerFrom).execute();
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
