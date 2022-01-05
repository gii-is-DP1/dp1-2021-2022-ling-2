package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.command.AttackPhaseEnd;
import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 3
 * Pierdes 1 carta. Finaliza el ataque.
 * 
 * @author Pablosancval
 */
@Component
public class DisparoCertero {

    @Autowired
    GameService gameService;

    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(3, playerFrom, targetedEnemy).execute();
        new AttackPhaseEnd(gameService, playerFrom).execute();
    }
}
