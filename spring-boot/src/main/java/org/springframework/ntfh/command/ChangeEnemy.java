package org.springframework.ntfh.command;

import java.util.List;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChangeEnemy implements Command{
    
    public Player playerFrom; //puede que no completamente necesario
    public EnemyIngame targetedEnemy;
    public Game game;

    @Override
    public void execute() {
        List<EnemyIngame> enemiesFighting = game.getEnemiesFighting();
        List<EnemyIngame> enemiesInPile = game.getEnemiesInPile();
        EnemyIngame lastEnemy = enemiesInPile.get(-1);
        enemiesFighting.remove(targetedEnemy);
        enemiesFighting.add(lastEnemy);
        enemiesInPile.remove(lastEnemy);
        enemiesInPile.add(targetedEnemy);
        game.setEnemiesFighting(enemiesFighting);
        game.setEnemiesInPile(enemiesInPile);
    }
    
}
