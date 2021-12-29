package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class FlechaCorrosiva {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy){
        new DealDamageCommand(1, targetedEnemy);
        new DiscardCommand(1, playerFrom);
        //TODO condición avanzada incremento de daño de todas las fuentes, podría añadir un atributo al enemigo
        //que se elimine al final del turno que hace que siempre trague uno mas de daño. Aunque esto podría generar
        //problemas
    }
    
}
