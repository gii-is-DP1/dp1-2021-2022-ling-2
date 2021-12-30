package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.List;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class BolaDeFuego {
    public void execute(Player playerFrom, Game game, AbilityCardIngame cardPlayed){
        List<EnemyIngame> targetList = game.getEnemiesFighting();
        for(EnemyIngame target:targetList){
            new DealDamageCommand(2, target).execute();
        }
        List<Player> targets = game.getPlayers();
        targets.remove(playerFrom);
        for(Player target:targets){
            new DiscardCommand(1, target).execute();
        }
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
