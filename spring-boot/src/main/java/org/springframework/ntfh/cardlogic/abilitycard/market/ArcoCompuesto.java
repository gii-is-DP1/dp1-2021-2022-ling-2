package org.springframework.ntfh.cardlogic.abilitycard.market;

import java.util.List;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.stereotype.Component;

/**
 * Daño: 4
 * Modificador: Ranged
 * 
 * @author Pablosancval
 */
@Component
public class ArcoCompuesto {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        List<ProficiencyTypeEnum> neededProfs = List.of(ProficiencyTypeEnum.RANGED);
        Integer damageModifier = playerFrom.getCharacterType().getProficiencies().stream()
                .filter(proficiency -> neededProfs.contains(proficiency.getProficiencyTypeEnum()))
                .findFirst().orElseThrow().getSecondaryDebuff();
        new DealDamageCommand(4 + damageModifier, playerFrom, targetedEnemy).execute();
    }
}
