package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class Escudo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed){
        new RestrainCommand(targetedEnemy).execute();
        new PlayedCommand(playerFrom, cardPlayed).execute();
        //TODO Fin de la fase de ataque
    }
}
