package org.springframework.ntfh.cardlogic.abilitycard.warrior;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

@Component
public class TodoONada {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        Integer extraDamage = playerFrom.getAbilityPile().get(0).getBaseDamage();
        new DealDamageCommand(1+extraDamage, playerFrom, targetedEnemy).execute();
    }
}
