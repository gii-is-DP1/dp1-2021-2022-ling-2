package org.springframework.ntfh.cardlogic.abilitycard.rogue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.command.Command;
import org.springframework.ntfh.command.StealCoinCommand;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.stereotype.Component;

// Roba 1 moneda a cada héroe
@Component
public class RobarBolsillos implements Command {
    // private Integer gameId;
    // private Player playerFrom;

    @Autowired
    GameService gameService;

    // public RobarBolsillos(Integer gameId, Player playerFrom) {
    // this.gameId = gameId;
    // this.playerFrom = playerFrom;
    // }

    @Override
    public void execute() {
        // gameService.findPlayersByGameId(gameId).forEach(player -> {
        // new StealCoinCommand(playerFrom, player).execute();
        // });
    }
}
