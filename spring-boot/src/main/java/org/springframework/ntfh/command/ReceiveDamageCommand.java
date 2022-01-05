package org.springframework.ntfh.command;

import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReceiveDamageCommand implements Command {

    private Integer damage;

    private EnemyIngame enemyFrom;

    private Player playerTo;

    @Override
    public void execute() {
        Integer guard = playerTo.getGuard();
        if (enemyFrom.getRestrained())
            damage = 0;

        if (damage >= guard){
            damage = damage-guard;
        } else {
            damage = 0;
            guard = guard-damage;
            playerTo.setGuard(guard);
        }

        for (int i = 0; i < damage; i++) {
            // "Top card" is the last card in the abilityPile list
            Integer topCardIndex = playerTo.getAbilityPile().size() - 1;
            AbilityCardIngame topCard = playerTo.getAbilityPile().get(topCardIndex);

            playerTo.getAbilityPile().remove(topCard);
            playerTo.getDiscardPile().add(topCard);

            if (playerTo.getAbilityPile().isEmpty()) {
                new GiveWoundCommand(playerTo).execute();
                break;
            }
        }
    }

}
