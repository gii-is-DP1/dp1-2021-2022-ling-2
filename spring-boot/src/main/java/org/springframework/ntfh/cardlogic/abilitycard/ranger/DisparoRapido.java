package org.springframework.ntfh.cardlogic.abilitycard.ranger;

import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.ReturnedToAbilityPileCommand;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.player.Player;

/**
 * Daño: 1
 * Roba una carta. Si es "disparo rápido", úsala. Si no, ponla en el fondo de tu
 * mazo de Habilidad.
 * 
 * @author Pablosancval
 */
public class DisparoRapido {
    public void execute(Player playerFrom, EnemyIngame targetedEnemy) {
        new DealDamageCommand(1, playerFrom, targetedEnemy).execute();
        new DrawCommand(1, playerFrom).execute();
        Integer posicion = playerFrom.getHand().size()-1;
        AbilityCardIngame cartaRobada = playerFrom.getHand().get(posicion);
        if(cartaRobada.getAbilityCardTypeEnum().equals(AbilityCardTypeEnum.DISPARO_RAPIDO)){
        //    execute(playerFrom, targetedEnemy);
        //La carta por algún motivo ejecuta las instancias de la misma pero no son handleadas para que se
        //descarten como la carta jugada, propongo únicamente dejarla en la mano y que el jugador la juegue
        //de manera regular. También al parecer ignora el
        } else {
            new ReturnedToAbilityPileCommand(playerFrom, cartaRobada.getAbilityCardTypeEnum());
        }
    }
}
