package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import java.util.List;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;

/**
 * Daño: 2
 * Dañas a 2 enemigos y al héroe con menos Heridas (en caso de empate, tú
 * eliges)
 * 
 * @author Pablosancval
 */
public class LluviaDeFlechas {
    public void execute(Player playerFrom) {
        Game game = playerFrom.getGame();
        List<EnemyIngame> targetList = game.getEnemiesFighting();
        for (EnemyIngame target : targetList) {
            new DealDamageCommand(2, playerFrom, target).execute();
        }
        // TODO daño al aliado con menos heridas en caso de empate elección
    }
}
