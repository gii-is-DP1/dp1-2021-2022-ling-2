package org.springframework.ntfh.entity.turn;

public interface TurnState {
    void refreshMarket();
    void refreshEnemy();
    void changePlayer();
    void changeScene();
}