package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import java.util.stream.IntStream;

import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.GiveGloryCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Todos recuperan 2 cartas. Roba 1 ficha y ganas 1 ficha de Gloria.
 * 
 * @author Pablosancval
 */
@Component
public class VozDeAliento {
    public void execute(Player playerFrom) {
        Game game = playerFrom.getGame();
        new DrawCommand(1, playerFrom).execute();
        new GiveGloryCommand(1, playerFrom).execute();
        game.getPlayers().forEach(player -> IntStream.range(0, 2).forEach(i -> new RecoverCommand(player).execute()));
    }
}
