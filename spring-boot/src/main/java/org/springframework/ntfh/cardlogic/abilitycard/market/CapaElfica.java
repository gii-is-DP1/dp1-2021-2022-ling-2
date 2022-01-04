package org.springframework.ntfh.cardlogic.abilitycard.market;

import java.util.Iterator;

import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Modificador: Spell, Ranged
 * El enemigo seleccionado no causa daño este turno.
 * 
 * @author Pablosancval
 */
@Component
public class CapaElfica {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        // TODO replace iterator
        Iterator<Proficiency> iterador = playerFrom.getCharacterType().getProficiencies().iterator();
        while (iterador.hasNext()) {
            ProficiencyTypeEnum tipo = iterador.next().getProficiencyTypeEnum();
            if (tipo.equals(ProficiencyTypeEnum.DEXTERITY) || tipo.equals(ProficiencyTypeEnum.SPELL)) {
                new RestrainCommand(targetedEnemy).execute();
            }
        }
    }
}
