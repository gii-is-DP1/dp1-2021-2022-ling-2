package org.springframework.ntfh.cardlogic.abilitycard.warrior;


import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;



@Component
public class Espadazo {

    
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DealDamageCommand(1, targetedEnemy).execute();        
        
        // Long instances= targetedEnemy.getPlayedCardsOnMeInTurn().stream().filter(x -> x.equals(cardIngame)).collect(Collectors.counting());
        
        if(!targetedEnemy.getPlayedCardsOnMeInTurn().contains(AbilityCardTypeEnum.ESPADAZO)){
            new DrawCommand(1, playerFrom).execute();
        }
        
        playerFrom.getGame().getEnemiesFighting().stream().forEach(x-> x.getPlayedCardsOnMeInTurn().add(AbilityCardTypeEnum.ESPADAZO));

        
    }
}
