package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import java.util.List;

import org.springframework.ntfh.command.GetGoldCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class SaqueoOro {
    public void execute(Player playerFrom){
        Game game = playerFrom.getGame();
        List<EnemyIngame> listEnemiesFighting = game.getEnemiesFighting();
        Integer nEnemies = listEnemiesFighting.size();
        new GetGoldCommand(nEnemies*2, playerFrom).execute();
    }
}
