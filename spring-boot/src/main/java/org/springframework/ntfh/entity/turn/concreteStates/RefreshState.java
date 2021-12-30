package org.springframework.ntfh.entity.turn.concreteStates;

import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnState;

public class RefreshState implements TurnState {

    Turn turn;
    public RefreshState(Turn turn) {
        this.turn = turn;
    }

    @Override
    public void button() {
        // TODO Refresh board for new turn


        
        turn.setState(turn.getPlayerState());
    }

    
}
