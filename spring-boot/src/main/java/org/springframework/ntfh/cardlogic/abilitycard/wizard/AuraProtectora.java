package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.List;

import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Cancela todo el Daño sufrido durante el próximo ataque de la Horda. Pierdes 1
 * carta por cada enemigo.
 * 
 * @author Pablosancval
 */
@Component
public class AuraProtectora {
    public void execute(Player playerFrom) {
        Game game = playerFrom.getGame();
        Integer numDiscards = game.getEnemiesFighting().size();
        new DiscardCommand(numDiscards, playerFrom).execute();
        List<EnemyIngame> targetList = game.getEnemiesFighting();
        for (EnemyIngame target : targetList) {
            new RestrainCommand(target).execute();
        }
    }
}
