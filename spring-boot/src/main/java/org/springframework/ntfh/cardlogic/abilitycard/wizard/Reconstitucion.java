package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.stream.IntStream;

import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Roba 1 carta. Recuperas 2 cartas.
 * 
 * @author Pablosancval
 */
@Component
public class Reconstitucion {
    public void execute(Player playerFrom) {
        new DrawCommand(1, playerFrom).execute();
        IntStream.range(0, 2).forEach(i -> new RecoverCommand(playerFrom).execute());
    }
}
