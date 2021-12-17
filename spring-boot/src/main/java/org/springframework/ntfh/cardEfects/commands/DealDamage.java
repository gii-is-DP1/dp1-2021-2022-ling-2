package org.springframework.ntfh.cardEfects.commands;
import org.springframework.ntfh.cardEfects.CardNameEnumerate;
import org.springframework.ntfh.cardEfects.Commad;
import org.springframework.ntfh.cardEfects.OperationsReceiver;
import org.springframework.ntfh.entity.model.EnemyEntity;
import org.springframework.ntfh.entity.player.Player;

public class DealDamage implements Commad {
    
    private Player player;//Player that is playing the card 
    private Character character; //Character that is using said card
    private EnemyEntity enemy; //Enemy that is the objetive of said card
    private Integer damage;
    private OperationsReceiver operationsReceiver;
    public void DealDamageCommand(OperationsReceiver operationsReceiver){
        this.operationsReceiver=operationsReceiver;
    }

    @Override
    public void execute() {
        operationsReceiver.dealDamage(player, character, enemy, damage);//TODO
    }

}
