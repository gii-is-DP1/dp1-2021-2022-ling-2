package org.springframework.ntfh.cardlogic.abilitycard.market;

import java.util.Iterator;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class AlabardaOrca {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        Iterator<Proficiency> iterador = playerFrom.getCharacterType().getProficiencies().iterator();
        Integer damageModifier = 0;
        while(iterador.hasNext()){
            ProficiencyTypeEnum tipo= iterador.next().getProficiencyTypeEnum();
            if(tipo.equals(ProficiencyTypeEnum.MELEE)){
                damageModifier = iterador.next().getSecondaryDebuff();
                break;
            }
        }
        new DealDamageCommand(4+damageModifier, targetedEnemy).execute();
    }    
}
