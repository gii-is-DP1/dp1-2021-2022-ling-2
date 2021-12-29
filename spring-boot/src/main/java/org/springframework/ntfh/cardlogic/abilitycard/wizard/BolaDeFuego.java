package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.List;

import org.springframework.ntfh.command.DealDamageAOECommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class BolaDeFuego {
    public void execute(Player playerFrom, Game game){
        List<EnemyIngame> EnemiesFighting = game.getEnemiesFighting();
        new DealDamageAOECommand(2, EnemiesFighting).execute();
        List<Player> targets = game.getPlayers();
        targets.remove(playerFrom);
        for(Player target:targets){
            new DiscardCommand(1, target).execute();
        }
    }
}
