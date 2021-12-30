package org.springframework.ntfh.entity.turn.concreteStates;

import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnState;

public class MarketState implements TurnState{

    Turn turn;
    public MarketState(Turn turn) {
        this.turn = turn;
    }

    @Override
    public void button() {
        turn.setState(turn.getEnemyState());
    }

}
