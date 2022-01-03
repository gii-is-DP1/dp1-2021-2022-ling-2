package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.ChangeEnemy;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class Supervivencia {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        Game game = playerFrom.getGame();
        new ChangeEnemy(playerFrom, targetedEnemy, game).execute();
    }
}