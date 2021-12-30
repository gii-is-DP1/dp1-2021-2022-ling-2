package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

public class DisparoRapido {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed){
        new DealDamageCommand(1, targetedEnemy).execute();
        new DrawCommand(1, playerFrom).execute();
        new PlayedCommand(playerFrom, cardPlayed).execute();
        //TODO mirar si la carta robada es un disparo rápido, if(n=DisparoRapido){DisparoRapido().execute()}
    }
}
