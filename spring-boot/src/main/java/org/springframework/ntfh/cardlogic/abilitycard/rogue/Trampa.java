package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;

public class Trampa {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        targetedEnemy.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.TRAMPA);
    }
}
