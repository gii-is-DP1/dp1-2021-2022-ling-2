package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.GetGuardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;

public class EnLasSombras {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DealDamageCommand(1, targetedEnemy).execute();
        new GetGuardCommand(2, playerFrom).execute();
    }
}
