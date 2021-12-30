package org.springframework.ntfh.cardlogic.abilitycard.warrior;


import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class AtaqueBrutal {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed){
        new DealDamageCommand(3, targetedEnemy).execute();
        new DiscardCommand(1, playerFrom).execute();
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
