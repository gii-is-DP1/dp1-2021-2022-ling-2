package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.ReceiveDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;

public class Trampa {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        targetedEnemy.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.TRAMPA);
        new ReceiveDamageCommand(targetedEnemy.getCurrentEndurance(), targetedEnemy, playerFrom);
        new DealDamageCommand(100, playerFrom, targetedEnemy).execute();
    }
}
