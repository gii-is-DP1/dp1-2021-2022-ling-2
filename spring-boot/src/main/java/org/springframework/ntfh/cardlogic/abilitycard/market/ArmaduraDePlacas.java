package org.springframework.ntfh.cardlogic.abilitycard.market;

import java.util.stream.IntStream;

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
        IntStream.range(0, 4).forEach(i -> new RecoverCommand(playerFrom).execute());
    }
}
