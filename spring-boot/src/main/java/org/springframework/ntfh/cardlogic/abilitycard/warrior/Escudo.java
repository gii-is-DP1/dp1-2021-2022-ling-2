package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class Escudo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new RestrainCommand(targetedEnemy).execute();
        //TODO Fin de la fase de ataque
    }
}
