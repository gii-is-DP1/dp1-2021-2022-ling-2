package org.springframework.ntfh.cardEfects.commands;
import org.springframework.ntfh.cardEfects.CardNameEnumerate;
import org.springframework.ntfh.cardEfects.Command;
import org.springframework.ntfh.cardEfects.OperationsReceiver;
import org.springframework.ntfh.entity.model.EnemyEntity;
import org.springframework.ntfh.entity.player.Player;

public class DealDamage implements Command {
    
    private Player player;//Player that plays the card
    private EnemyEntity enemy; //Enemy that is the objetive of said card
    private Integer damage;
    private OperationsReceiver operationsReceiver;

    public DealDamage(Player player, EnemyEntity enemy, Integer damage){
        this.player=player;
        this.enemy=enemy;
        this.damage=damage;
    }

    public void DealDamageCommand(OperationsReceiver operationsReceiver){
        this.operationsReceiver=operationsReceiver;
    }


    @Override
    public void execute() {
        operationsReceiver.dealDamage(player,enemy, damage);//TODO
    }

}
