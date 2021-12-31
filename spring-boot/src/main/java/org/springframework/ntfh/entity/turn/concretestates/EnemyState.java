package org.springframework.ntfh.entity.turn.concretestates;

import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;

public class EnemyState implements TurnState {

    @Override
    public TurnStateType getNextState() {
        return TurnStateType.REFRESH_STATE;
    }

}
