package org.springframework.ntfh.cardlogic.abilitycard.market;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.HandToAbilityPileCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
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
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(2, playerFrom, targetedEnemy).execute();

        Boolean hasDexterity = playerFrom.getCharacterType().getProficiencies().stream()
                .anyMatch(proficiency -> proficiency.getProficiencyTypeEnum().equals(ProficiencyTypeEnum.DEXTERITY));

        if (hasDexterity)
            new HandToAbilityPileCommand(playerFrom, AbilityCardTypeEnum.DAGA_ELFICA).execute();
    }
}
