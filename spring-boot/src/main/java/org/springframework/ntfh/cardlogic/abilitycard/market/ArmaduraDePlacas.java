package org.springframework.ntfh.cardlogic.abilitycard.market;

import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Modificador: Melee
 * Recuperas 4 cartas
 * 
 * @author Pablosancval
 */
@Component
public class ArmaduraDePlacas {
    public void execute(Player playerFrom) {
        new RecoverCommand(4, playerFrom).execute();
    }
}
