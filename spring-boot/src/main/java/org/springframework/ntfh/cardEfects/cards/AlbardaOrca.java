package org.springframework.ntfh.cardEfects.cards;

import java.util.Set;

import org.springframework.ntfh.cardEfects.CardProficiencyEnumerate;
import org.springframework.ntfh.cardEfects.commands.DealDamage;
import org.springframework.ntfh.entity.model.EnemyEntity;
import org.springframework.ntfh.entity.player.Player;

public class AlbardaOrca {
    
    
    //The cards are the invoker of the command pattern but it doesn´t make sense that it´s also has enemy objective so how we should deal with it?
    //And we should have hear the price and profiency of the card?

    private Player player;
    private Integer damage;
    private Integer price;
    private Set<CardProficiencyEnumerate> profiency;
    private EnemyEntity enemy;
    // Todo the price and profiency of the card shoould be taking care by the the buy mechaish? 

    public AlbardaOrca(){
        this.damage=4;
        this.price=5;
        Set<CardProficiencyEnumerate> profiencies=Set.of(CardProficiencyEnumerate.MELEE);
        this.profiency=profiencies;
    }

    public void setPlayer(Player player){
        this.player=player;
    }

    public void setTarget(EnemyEntity enemy){
        this.enemy=enemy;
    }

    public void execute(){
        DealDamage c=new DealDamage(player, enemy, damage);
        c.execute();
    }


}
