package org.springframework.ntfh.cardlogic.abilitycard.market;

import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.RecoverCardCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Modificador: -
 * Busca una carta en tu pila de Desgaste y ponla en tu mano
 * 
 */
@Component
public class VialDeConjuracion {
    public void execute(Player playerFrom){
        new RecoverCommand(playerFrom).execute();
        new DrawCommand(1, playerFrom).execute();
    }
}
