package org.springframework.ntfh.entity.turn;

public interface TurnState {
    TurnStateType getNextState();

    public void playCard(Integer abilityCardIngameId, Integer enemyId);

    public void buyMarketCard(Integer marketCardIngameId, String token);
}