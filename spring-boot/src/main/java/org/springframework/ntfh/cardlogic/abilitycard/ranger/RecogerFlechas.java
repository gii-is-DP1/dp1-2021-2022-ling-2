package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.GiveGoldCommand;
import org.springframework.ntfh.command.RecoverCardCommand;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;

/**
 * Daño: -
 * Recupera una carta de "Disparo rápido" de tu pila de Desgaste. Baraja el Mazo
 * de Habilidades. Gana 1 moneda.
 * 
 * @author Pablosancval
 */
public class RecogerFlechas {
    public void execute(Player playerFrom) {
        new GiveGoldCommand(1, playerFrom).execute();
        new RecoverCardCommand(playerFrom, AbilityCardTypeEnum.DISPARO_RAPIDO).execute();
    }
}
