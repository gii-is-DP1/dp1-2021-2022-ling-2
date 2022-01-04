package org.springframework.ntfh.entity.turn;

import org.springframework.ntfh.entity.game.Game;

public interface TurnState {
    public void preState(Game game);

    public void postState(Game game);

    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token);

    public void buyMarketCard(Integer marketCardIngameId, String token);

    public TurnStateType getNextState();
}