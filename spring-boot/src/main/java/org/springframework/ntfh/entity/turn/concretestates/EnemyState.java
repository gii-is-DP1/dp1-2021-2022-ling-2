package org.springframework.ntfh.entity.turn.concretestates;

import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.stereotype.Component;

@Component
public class EnemyState implements TurnState {

    @Override
    public TurnStateType getNextState() {
        return TurnStateType.REFRESH_STATE;
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId) {
        throw new IllegalStateException("You can't play a card in the enemy attack stage");
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        throw new IllegalStateException("You can't buy a card in the enemy attack stage");
    }

}
