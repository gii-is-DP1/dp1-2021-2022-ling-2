package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GetGoldCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.command.RecoverQSCommand;
import org.springframework.ntfh.entity.player.Player;

public class RecogerFlechas {
    public void execute(Player playerFrom){
        new GetGoldCommand(1, playerFrom).execute();
        new RecoverQSCommand(playerFrom).execute();
    }
}
