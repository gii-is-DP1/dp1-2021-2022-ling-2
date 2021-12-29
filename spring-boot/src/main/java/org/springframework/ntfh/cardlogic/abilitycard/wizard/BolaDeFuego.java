package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import java.util.List;

import org.springframework.ntfh.command.DealDamageAOECommand;
import org.springframework.ntfh.command.FriendlyFireAOECommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;

public class BolaDeFuego {
    public void execute(Player playerFrom, Game game){
        List<EnemyIngame> EnemiesFighting = game.getEnemiesFighting();
        new DealDamageAOECommand(2, EnemiesFighting).execute();
        List<Player> players = game.getPlayers();
        new FriendlyFireAOECommand(1,players, playerFrom);
    }
}
