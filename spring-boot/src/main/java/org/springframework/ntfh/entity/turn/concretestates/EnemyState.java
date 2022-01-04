package org.springframework.ntfh.entity.turn.concretestates;

import org.springframework.ntfh.entity.game.Game;
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
    public void preState(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postState(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
        throw new IllegalStateException("You can't play cards now");
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        throw new IllegalStateException("You can't buy cards now");
    }
}
