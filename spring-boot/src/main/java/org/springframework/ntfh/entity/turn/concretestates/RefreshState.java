package org.springframework.ntfh.entity.turn.concretestates;

import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.stereotype.Component;

@Component
public class RefreshState implements TurnState {

    @Override
    public TurnStateType getNextState() {
        return TurnStateType.PLAYER_STATE;
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        // TODO Auto-generated method stub

    }

}
