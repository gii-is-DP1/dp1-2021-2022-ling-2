package org.springframework.ntfh.cardEfects;

import org.springframework.ntfh.entity.model.EnemyEntity;
import org.springframework.ntfh.entity.player.Player;

public class OperationsReceiver {
    


    public void dealDamage(Player p, EnemyEntity e, Integer damage){
        Integer endurance= e.getEndurance();
        if(endurance-damage==0){
            if(e.hasHiddenGlory()){
                Integer gloryValue=e.getHidenGloryValue();
                giveGlory(p, gloryValue);
            }

            if(e.hasHiddenGold()){
                Integer goldValue=e.getHidenGoldValue();
                giveGlory(p, goldValue);
            }
            
                
        }
        e.setEndurance(endurance-damage);
    }


    public void giveGlory(Player p, Integer value){
        Integer glory=p.getGlory();
        p.setGlory(glory+value);
    }

    public void giveGold(Player p, Integer value){
        Integer gold=p.getGold();
        p.setGold(gold+value);
    }

}
