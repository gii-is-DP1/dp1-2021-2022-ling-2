package org.springframework.ntfh.entity.game.concretestates;

import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameState;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@State
public class OngoingState implements GameState {

    @Autowired
    private UserService userService;

    @Autowired
    private TurnService turnService;

    @Override
    public void preState(Game game) {
        // TODO create the first turn bla bla
        turnService.initializeFromGame(game);
        log.info("Game with id " + game.getId() + " was created with players: " + game.getPlayers());
    }

    @Override
    public GameStateType getNextState() {
        return GameStateType.FINISHED;
    }

    @Override
    public void deleteGame(Integer gameId) {
        throw new IllegalStateException("A game can't be deleted while it is ongoing");
    }

    @Override
    public Game joinGame(Integer gameId, String username) {
        throw new IllegalStateException("You can't join a game that has already started");
    }

    @Override
    public Game removePlayer(Integer gameId, String username) {
        throw new IllegalStateException("A player can't be removed after the game has started");
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
        String username = TokenUtils.usernameFromToken(token);
        Player player = userService.findUser(username).getPlayer();
        Turn currentTurn = player.getGame().getCurrentTurn();
        TurnState turnState = turnService.getState(currentTurn);
        turnState.playCard(abilityCardIngameId, enemyId, token);
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        String username = TokenUtils.usernameFromToken(token);
        Player player = userService.findUser(username).getPlayer();
        Turn currentTurn = player.getGame().getCurrentTurn();
        TurnState turnState = turnService.getState(currentTurn);
        turnState.buyMarketCard(marketCardIngameId, token);
    }

    @Override
    public Game startGame(Integer gameId) {
        throw new IllegalStateException("A game can't be started while it is ongoing");
    }

    @Override
    public void finishGame(Game game) {
        game.setFinishTime(Timestamp.from(Instant.now()));
        game.getPlayers().forEach(p -> {
            User user = p.getUser();
            user.setPlayer(null);
        });
        game.setStateType(GameStateType.FINISHED);
    }

}
