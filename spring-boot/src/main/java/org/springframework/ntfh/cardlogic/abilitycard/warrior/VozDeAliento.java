package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import java.util.List;

import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.GetGloryCommand;
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
        new GetGloryCommand(1, playerFrom).execute();
        List<Player> players = game.getPlayers();
        for (Player playerTarget : players) {
            new RecoverCommand(2, playerTarget).execute();
        }
    }
}
