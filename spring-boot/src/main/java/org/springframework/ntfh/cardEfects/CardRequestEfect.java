package org.springframework.ntfh.cardEfects;

import org.springframework.ntfh.entity.model.EnemyEntity;
import org.springframework.ntfh.entity.player.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequestEfect {
    private Player player;//Player that is playing the card 
    private Character character; //Character that is using said card
    private EnemyEntity enemy; //Enemy that is the objetive of said card
    private CardNameEnumerate name; //Identifier of said card
    

    public CardRequestEfect(Player p, Character c, EnemyEntity e, CardNameEnumerate name){
        this.player= p;
        this.character= c;
        this.enemy= e;
        this.name= name;

    }
}
