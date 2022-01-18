package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.command.StealCoinCommand;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

/**
 * Daño: -
 * Roba 1 moneda a cada héroe
 * 
 * @author andrsdt
 */
@Component
public class RobarBolsillos {

    @Autowired
    private GameService gameService;

    public void execute(Player playerFrom) {
        Integer gameId = playerFrom.getGame().getId();
        gameService.findPlayersByGameId(gameId).forEach(player -> 
            new StealCoinCommand(playerFrom, player).execute());
    }
}
