package org.springframework.ntfh.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.player.Player;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AttackPhaseEndCommand implements Command{

    @Autowired
    GameService gameService;

    private Player playerFrom;

    @Override
    public void execute() {
        Integer gameId = playerFrom.getGame().getId();
        gameService.setNextTurnState(gameService.getCurrentTurnByGameId(gameId));
    }
}
