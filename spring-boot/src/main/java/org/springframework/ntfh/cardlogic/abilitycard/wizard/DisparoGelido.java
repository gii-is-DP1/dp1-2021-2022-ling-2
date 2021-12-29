package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class DisparoGelido {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DealDamageCommand(1, targetedEnemy).execute();
        new DrawCommand(1, playerFrom).execute();
        //TODO protection es un comando mas avanzado que aún no se me ocurre como implementar
    }
    
}
