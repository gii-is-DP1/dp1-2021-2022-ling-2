package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class TodoONada {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DrawCommand(1, playerFrom).execute();
        Integer drawnCardPosition = playerFrom.getHand().size()-1;
        Integer extraDamage = playerFrom.getHand().get(drawnCardPosition).getBaseDamage();
        new DealDamageCommand(1+extraDamage, playerFrom, targetedEnemy).execute();
    }
}
