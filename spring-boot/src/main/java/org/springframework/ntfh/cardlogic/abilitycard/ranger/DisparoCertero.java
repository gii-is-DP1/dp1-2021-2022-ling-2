package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class DisparoCertero {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed){
        new DealDamageCommand(3, targetedEnemy).execute();
        new PlayedCommand(playerFrom, cardPlayed).execute();
        //TODO comando de fin de ataque
    }
}
