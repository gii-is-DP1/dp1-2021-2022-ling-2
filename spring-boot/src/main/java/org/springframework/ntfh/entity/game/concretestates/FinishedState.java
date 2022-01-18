package org.springframework.ntfh.entity.game.concretestates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameState;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.stereotype.Component;

@Component
public class FinishedState implements GameState {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Override
    public void preState(Game game) {
        // TODO What to do when a game is finished?

    }

    @Override
    public GameStateType getNextState() {
        throw new IllegalStateException("There are not more states after the game has finished");
    }

    @Override
    public void deleteGame(Integer gameId) {
        Game game = gameService.findGameById(gameId);
        game.getPlayers().forEach(p -> {
            p.getUser().setPlayer(null);
            playerService.delete(p);
        }); // TODO player remains orfan
        gameService.delete(game);
    }

    @Override
    public Game joinGame(Integer gameId, String username, String token) {
        throw new IllegalStateException("You can't join a finished game");

    }

    @Override
    public Game removePlayer(Integer gameId, String username, String token) {
        throw new IllegalStateException("You can't leave from a finished game");

    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
        throw new IllegalStateException("You can't play cards in a finished game");
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        throw new IllegalStateException("You can't buy cards in a finished game");
    }

    @Override
    public Game startGame(Integer gameId) {
        throw new IllegalStateException("A finished game can't be started");
    }

    @Override
    public void finishGame(Game game) {
        throw new IllegalStateException("A finished game can't be finished");
    }

}