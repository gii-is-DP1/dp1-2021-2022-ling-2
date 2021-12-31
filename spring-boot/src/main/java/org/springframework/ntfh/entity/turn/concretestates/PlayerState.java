package org.springframework.ntfh.entity.turn.concretestates;

import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnState;

public class PlayerState implements TurnState {

    Turn turn;

    public PlayerState(Turn turn) {
        this.turn = turn;
    }

    @Override
    public void button() {
        turn.setState(turn.getMarketState());
    }

}
