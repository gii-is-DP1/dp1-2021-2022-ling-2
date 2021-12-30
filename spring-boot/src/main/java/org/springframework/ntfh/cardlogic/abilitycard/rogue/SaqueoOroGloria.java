package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import java.util.List;

import org.springframework.ntfh.command.GetGloryCommand;
import org.springframework.ntfh.command.GetGoldCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class SaqueoOroGloria {
    public void execute(Player playerFrom, Game game, AbilityCardIngame cardPlayed){
        List<EnemyIngame> listEnemiesFighting = game.getEnemiesFighting();
        Integer nEnemies = listEnemiesFighting.size();
        new GetGoldCommand(nEnemies, playerFrom).execute();
        new GetGloryCommand(nEnemies, playerFrom).execute();
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
