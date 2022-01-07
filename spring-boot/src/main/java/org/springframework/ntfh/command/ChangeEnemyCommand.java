package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChangeEnemyCommand implements Command {

    private Player playerFrom; // puede que no completamente necesario

    private EnemyIngame targetedEnemy;

    @Override
    public void execute() {
        Game game = playerFrom.getGame();
        List<EnemyIngame> enemiesInPile = game.getEnemiesInPile();
        if (enemiesInPile.size() >= 2) {
            List<EnemyIngame> enemiesFighting = game.getEnemiesFighting();
            Integer lastEnemyPosition = enemiesInPile.size()-2;
            EnemyIngame lastEnemy = enemiesInPile.get(lastEnemyPosition);
            enemiesFighting.remove(targetedEnemy);
            enemiesFighting.add(lastEnemy);
            enemiesInPile.remove(lastEnemy);
            enemiesInPile.add(lastEnemyPosition, targetedEnemy);
            game.setEnemiesFighting(enemiesFighting);
            game.setEnemiesInPile(enemiesInPile);
        }
    }
}
