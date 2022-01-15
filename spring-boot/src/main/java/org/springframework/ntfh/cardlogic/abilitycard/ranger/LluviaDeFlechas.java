package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 2
 * Dañas a 2 enemigos y al héroe con menos Heridas (en caso de empate, tú
 * eliges)
 * 
 * @author Pablosancval
 */
@Component
public class LluviaDeFlechas {
    public void execute(Player playerFrom) {
        Game game = playerFrom.getGame();
        List<EnemyIngame> enemiesFighting = game.getEnemiesFighting();
        List<EnemyIngame> targetList = new ArrayList<>();
        for(EnemyIngame enemy : enemiesFighting){
            targetList.add(enemy);
        }
        for (EnemyIngame target : targetList) {
            new DealDamageCommand(2, playerFrom, target).execute();
        }
        List<Player> playersAlive = game.getAlivePlayersInTurnOrder();
        Integer minNumWounds = 2;
        List<Player> healthyMembers = new ArrayList<>();
        for(Player player:playersAlive){
            if(player.getWounds()==minNumWounds){
                healthyMembers.add(player);
            } else if(player.getWounds()<minNumWounds){
                minNumWounds = player.getWounds();
                healthyMembers.clear();
                healthyMembers.add(player);
            }
        }

        for(Player player:healthyMembers){
            new DiscardCommand(2, player).execute();
        }
    }
}
