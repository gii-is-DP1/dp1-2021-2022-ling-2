package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.ChangeEnemyCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Cambia uno de los enemigos en el campo de batalla por la carta inferior del
 * mazo de la Horda.
 * 
 * @author Pablosancval
 */
@Component
public class Supervivencia {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new ChangeEnemyCommand(playerFrom, targetedEnemy).execute();
    }
}