package org.springframework.ntfh.entity.game.concretestates;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
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

    @Autowired
    private GameService gameService;

    @Override
    public void preState(Game game) {
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
    public Game joinGame(Game game, User user) {
        throw new IllegalStateException("You can't join a game that has already started");
    }

    @Override
    public Game removePlayer(Integer gameId, String username) {
        throw new IllegalStateException("A player can't be removed after the game has started");
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
        String username = TokenUtils.usernameFromToken(token);
        Player player = userService.findByUsername(username).getPlayer();
        Turn currentTurn = player.getGame().getCurrentTurn();
        TurnState turnState = turnService.getState(currentTurn);
        turnState.playCard(abilityCardIngameId, enemyId, token);
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        String username = TokenUtils.usernameFromToken(token);
        Player player = userService.findByUsername(username).getPlayer();
        Turn currentTurn = player.getGame().getCurrentTurn();
        TurnState turnState = turnService.getState(currentTurn);
        turnState.buyMarketCard(marketCardIngameId, token);
    }

    @Override
    public Game startGame(Integer gameId) {
        throw new IllegalStateException("A game can't be started while it is ongoing");
    }

    @Override
    public Game finishGame(Game game) {
        List<Player> players = game.getPlayers();

        // Give +1 aditional glory to players with 0 wounds
        players.stream().filter(player -> player.getWounds() == 0)
                .forEach(player -> player.setGlory(player.getGlory() + 1));

        // Give +1 aditional glory every 3 coins
        players.stream().forEach(player -> {
            Integer additionalGlory = player.getGold() / 3;
            player.setGlory(player.getGlory() + additionalGlory);
        });

        // Choose as the winner the one with more glory. If there is a tie, the one alive wins
        Player winner =
                players.stream().max(compareByGlory().thenComparing(compareByKills()).thenComparing(compareByAlive()))
                        .orElse(game.getLeader());
        game.setWinner(winner);
        game.setFinishTime(Timestamp.from(Instant.now()));
        gameService.setNextState(game); // set state to FINISHED
        return gameService.save(game);
    }

    private Comparator<Player> compareByGlory() {
        return (p1, p2) -> p1.getGlory().compareTo(p2.getGlory());
    }

    private Comparator<Player> compareByKills() {
        return (p1, p2) -> p1.getKills().compareTo(p2.getKills());
    }

    private Comparator<Player> compareByAlive() {
        return (p1, p2) -> p2.isDead().compareTo(p1.isDead());
    }
}
