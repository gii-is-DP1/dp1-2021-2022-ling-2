package org.springframework.ntfh.cardlogic.abilitycard.market;

import org.springframework.ntfh.command.ExileCommand;
import org.springframework.ntfh.command.HealCommand;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * (Exiliable)
 * Daño: -
 * Modificador: -
 * Retira una ficha de Herida de tu Héroe
 * 
 */
@Component
public class PocionCurativa {
    public void execute(Player playerFrom) {
        new HealCommand(playerFrom).execute();
        new ExileCommand(playerFrom, AbilityCardTypeEnum.POCION_CURATIVA).execute();
    }
}
