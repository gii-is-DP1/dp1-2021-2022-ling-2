package org.springframework.ntfh.entity.turn.concretestates;

import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.stereotype.Component;

@Component
public class PlayerState implements TurnState {

    @Override
    public TurnStateType getNextState() {
        return TurnStateType.MARKET_STATE;
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId) {
        throw new IllegalStateException("TBD");
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        throw new IllegalStateException("You can't buy a card in the attack stage");
    }

}
