package org.springframework.ntfh.entity.game;

import org.springframework.ntfh.entity.user.User;

public interface GameState {

    public void preState(Game game);

    public GameStateType getNextState();

    public void deleteGame(Integer gameId);

    public Game joinGame(Game game, User user);

    public Game removePlayer(Integer gameId, String username);

    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token);

    public void buyMarketCard(Integer marketCardIngameId, String token);

    public Game startGame(Integer gameId);

    public Game finishGame(Game game);
}
