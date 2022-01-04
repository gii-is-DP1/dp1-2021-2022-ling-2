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
        // We don't need to check for proficiencies, because the player must have had
        // the necessary proficiency to buy this card
        new RecoverCommand(4, playerFrom).execute();
    }
}
