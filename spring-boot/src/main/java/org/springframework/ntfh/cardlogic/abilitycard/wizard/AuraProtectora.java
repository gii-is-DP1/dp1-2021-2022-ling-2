package org.springframework.ntfh.cardlogic.abilitycard.wizard;

import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;


@Component
public class AuraProtectora {
    public void execute(Player playerFrom, Game game){
        Integer numDiscards = game.getEnemiesFighting().size();
        new DiscardCommand(numDiscards, playerFrom).execute();
        //TODO implementar la protección, cada una actua de una manera así que habrá que hablarlo
    }
}
