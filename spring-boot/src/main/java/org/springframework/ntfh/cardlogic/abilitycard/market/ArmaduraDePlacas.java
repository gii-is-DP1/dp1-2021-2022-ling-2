package org.springframework.ntfh.cardlogic.abilitycard.market;

import java.util.Iterator;

import org.springframework.ntfh.command.PlayedCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.stereotype.Component;


@Component
public class ArmaduraDePlacas {
    public void execute(Player playerFrom, AbilityCardIngame cardPlayed){
        Iterator<Proficiency> iterador = playerFrom.getCharacterType().getProficiencies().iterator();
        while(iterador.hasNext()){
            ProficiencyTypeEnum tipo = iterador.next().getProficiencyTypeEnum();
            if(tipo.equals(ProficiencyTypeEnum.MELEE)){
                new RecoverCommand(4, playerFrom).execute();
            }
        }
        new PlayedCommand(playerFrom, cardPlayed).execute();
    }
}
