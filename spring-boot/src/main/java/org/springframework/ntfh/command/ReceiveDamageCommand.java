package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.enemy.EnemyModifierType;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReceiveDamageCommand implements Command {

    private EnemyIngame enemyFrom;

    private Player playerTo;

    @Override
    public void execute() {
        Integer damage = enemyFrom.getCurrentEndurance();
        EnemyModifierType enemyModifier = enemyFrom.getEnemy().getEnemyModifierType();
        CharacterTypeEnum characterClass = playerTo.getCharacterTypeEnum();
        if (Boolean.TRUE.equals(enemyFrom.getRestrained()))
            damage = 0;

        if (enemyModifier != null && enemyModifier.equals(EnemyModifierType.MAGIC_ATTACKER_1)
                && characterClass.equals(CharacterTypeEnum.WIZARD)) {
            damage -= 1;
        } else if (enemyModifier != null && enemyModifier.equals(EnemyModifierType.MAGIC_ATTACKER_2)
                && characterClass.equals(CharacterTypeEnum.WIZARD)) {
            damage -= 2;
        }

        for (int i = 0; i < damage; i++) {
            Integer guard = playerTo.getGuard();
            if (guard > 0) {
                playerTo.setGuard(guard - 1);
                break;
            }

            // "Top card" is the last card in the abilityPile list
            Integer topCardIndex = playerTo.getAbilityPile().size() - 1;
            AbilityCardIngame topCard = playerTo.getAbilityPile().get(topCardIndex);

            playerTo.getAbilityPile().remove(topCard);
            playerTo.getDiscardPile().add(topCard);

            if (playerTo.getAbilityPile().isEmpty()) {
                new GiveWoundCommand(playerTo).execute();
            }
        }
    }
}
