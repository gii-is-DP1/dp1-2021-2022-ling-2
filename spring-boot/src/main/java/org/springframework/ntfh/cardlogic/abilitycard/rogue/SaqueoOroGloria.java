package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import java.util.List;

import org.springframework.ntfh.command.GiveGloryCommand;
import org.springframework.ntfh.command.GiveGoldCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Gana 1 Moneda de oro por cada enemigo vivo. Gana 1 Punto de Gloria.
 * 
 * @author Pablosancval
 */
@Component
public class SaqueoOroGloria {
    public void execute(Player playerFrom) {
        Game game = playerFrom.getGame();
        List<EnemyIngame> listEnemiesFighting = game.getEnemiesFighting();
        Integer nEnemies = listEnemiesFighting.size();
        new GiveGoldCommand(nEnemies, playerFrom).execute();
        new GiveGloryCommand(nEnemies, playerFrom).execute();
    }
}
