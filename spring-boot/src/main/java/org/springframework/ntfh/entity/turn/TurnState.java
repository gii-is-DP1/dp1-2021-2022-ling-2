package org.springframework.ntfh.entity.turn;

public interface TurnState {
    TurnStateType getNextState();
}