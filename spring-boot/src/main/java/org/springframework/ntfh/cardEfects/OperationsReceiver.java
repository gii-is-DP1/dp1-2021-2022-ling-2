package org.springframework.ntfh.cardEfects;

import org.springframework.ntfh.entity.model.EnemyEntity;
import org.springframework.ntfh.entity.player.Player;

public class OperationsReceiver {
    


    public void dealDamage(Player p, Character c, EnemyEntity e, Integer damage){
        Integer endurance= e.getEndurance();
        if(endurance-damage==0){
            if(e.hasHiddenGlory()){
                Integer gloryValue=e.getHidenGloryValue();
                gainGlory(p, gloryValue);
            }

            if(e.hasHiddenGold()){
                Integer goldValue=e.getHidenGoldValue();
                gainGlory(p, goldValue);
            }
            
                
        }
        e.setEndurance(endurance-damage);
    }


    public void gainGlory(Player p, Integer value){
        Integer glory=p.getGlory();
        p.setGlory(glory+value);
    }

    public void gainGold(Player p, Integer value){
        Integer gold=p.getGold();
        p.setGold(gold+value);
    }

}
