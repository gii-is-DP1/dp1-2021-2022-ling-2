package org.springframework.ntfh.cardlogic.abilitycard.market;

import java.util.List;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Modificador: -
 * +1 al Daño en las cartas (con Daño) que utilices este turno
 * 
 */
@Component
public class PiedraDeAmolar {
    public void execute(Player playerFrom){
        Game game = playerFrom.getGame();
        List<EnemyIngame> listEnemiesFighting = game.getEnemiesFighting();
        for(EnemyIngame enemy : listEnemiesFighting){
            enemy.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.PIEDRA_DE_AMOLAR);
        }
    }
}
