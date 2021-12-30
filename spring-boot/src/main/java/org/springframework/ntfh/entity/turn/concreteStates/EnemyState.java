package org.springframework.ntfh.entity.turn.concreteStates;

import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnState;

public class EnemyState implements TurnState {

    Turn turn;
    public EnemyState(Turn turn) {
        this.turn = turn;
    }

    @Override
    public void button() {
        // Nothing
    }
    
}
