package org.springframework.ntfh.entity.turn.concreteStates;

import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnState;

public class PlayerState implements TurnState{

    Turn turn;
    public PlayerState(Turn turn) {
        this.turn = turn;
    }

    @Override
    public void refreshMarket() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void refreshEnemy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void changePlayer() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void changeScene() {
        // TODO Auto-generated method stub
        
    }
    
}
