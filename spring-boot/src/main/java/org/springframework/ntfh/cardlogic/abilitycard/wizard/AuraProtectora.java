package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.List;

import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class AuraProtectora {
    public void execute(Player playerFrom, Game game){
        Integer numDiscards = game.getEnemiesFighting().size();
        new DiscardCommand(numDiscards, playerFrom).execute();
        List<EnemyIngame> targetList = game.getEnemiesFighting();
        for(EnemyIngame target:targetList){
            new RestrainCommand(target).execute();
        }
    }
}
