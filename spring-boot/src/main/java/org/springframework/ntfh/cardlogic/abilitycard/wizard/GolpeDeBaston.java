package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: 1
 * Si ya utilizaste "Golpe de bastón" contra el mismo enemigo, el daño de esta
 * carta es 2.
 * 
 * @author Pablosancval
 */
@Component
public class GolpeDeBaston {

    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        
        if(targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.GOLPE_DE_BASTON)){
            new DealDamageCommand(2, playerFrom, targetedEnemy).execute();
        }else{
            new DealDamageCommand(1, playerFrom, targetedEnemy).execute();
        }

        playerFrom.getGame().getEnemiesFighting()
                .forEach(x -> x.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.GOLPE_DE_BASTON));
    }
}
