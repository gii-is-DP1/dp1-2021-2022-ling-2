package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class Espadazo {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed){
        new DealDamageCommand(1, targetedEnemy).execute();
        new DrawCommand(1, playerFrom).execute();
        //TODO condición de repetición
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
