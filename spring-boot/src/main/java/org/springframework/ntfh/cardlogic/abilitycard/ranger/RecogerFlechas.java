package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.GetGoldCommand;
import org.springframework.ntfh.command.RecoverQSCommand;
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
        new GetGoldCommand(1, playerFrom).execute();
        new RecoverQSCommand(playerFrom).execute();
    }
}
