package org.springframework.ntfh.cardlogic.abilitycard.market;

import java.util.Iterator;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.ReturnedToAbilityPileCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class DagaElfica {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed){
        Iterator<Proficiency> iterador = playerFrom.getCharacterType().getProficiencies().iterator();
        new DealDamageCommand(2, targetedEnemy).execute();
        while(iterador.hasNext()){
            ProficiencyTypeEnum tipo = iterador.next().getProficiencyTypeEnum();
            if(tipo.equals(ProficiencyTypeEnum.DEXTERITY)){
                new ReturnedToAbilityPileCommand(playerFrom, cardPlayed).execute();
            }
        }
    }
}
