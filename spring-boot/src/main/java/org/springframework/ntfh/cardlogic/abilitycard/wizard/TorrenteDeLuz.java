package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.stream.IntStream;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GiveGloryCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * El resto de héroes recuperan 2 cartas. Ganas 1 ficha de Gloria.
 * 
 * @author Pablosancval
 */
@Component
public class TorrenteDeLuz {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        Game game = playerFrom.getGame();
        new DealDamageCommand(2, playerFrom, targetedEnemy).execute();
        new GiveGloryCommand(1, playerFrom).execute();
        game.getPlayers()
                .forEach(
                        targetPlayer -> IntStream.range(0, 2).forEach(i -> new RecoverCommand(targetPlayer).execute()));
    }
}
