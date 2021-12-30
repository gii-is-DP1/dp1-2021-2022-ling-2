package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.GetGoldCommand;
import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class Engañar {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed){
        if(playerFrom.getGold()>=2){
            new GetGoldCommand(-2, playerFrom).execute();
            new RestrainCommand(targetedEnemy).execute();
        }
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
