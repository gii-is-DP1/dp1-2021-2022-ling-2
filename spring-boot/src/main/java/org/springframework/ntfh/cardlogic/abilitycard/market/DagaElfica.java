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

/**
 * Daño: 2
 * Modificador: -
 * Si el heroe tiene PROFICIENCY DEXTERITY, recupera esta carta después de
 * jugarla
 * 
 * @author Pablosancval
 */
@Component
public class DagaElfica {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy, AbilityCardIngame cardPlayed) {
        // TODO prescindir del atributo cardPlayed de alguna forma. Esto no encaja con
        // ninguna estructura de las que se contemplan en el java reflection así que
        // daría error al ejecutarlo.
        Iterator<Proficiency> iterador = playerFrom.getCharacterType().getProficiencies().iterator();
        new DealDamageCommand(2, targetedEnemy).execute();
        while (iterador.hasNext()) {
            ProficiencyTypeEnum tipo = iterador.next().getProficiencyTypeEnum();
            if (tipo.equals(ProficiencyTypeEnum.DEXTERITY)) {
                new ReturnedToAbilityPileCommand(playerFrom, cardPlayed).execute();
            }
        }
    }
}
