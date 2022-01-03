package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class RecoverCommand implements Command{

    private Integer numRecovered;
    private Player playerFrom;

    @Override
    public void execute() {
        for(int i=0; i<numRecovered; i++){
            List<AbilityCardIngame> listDiscardPile = playerFrom.getDiscardPile();
            if(listDiscardPile.size()>=1){
                AbilityCardIngame recoveredCard = listDiscardPile.get(0);
                listDiscardPile.remove(0);
                List<AbilityCardIngame> listAbilityPile = playerFrom.getAbilityPile();
                listAbilityPile.add(recoveredCard);
                
                playerFrom.setAbilityPile(listAbilityPile);
                playerFrom.setDiscardPile(listDiscardPile);
            } else {
                break;
            }
        }
    }
    
}
