package org.springframework.ntfh.entity.game;

public interface GameState {

    public void preState(Game game);

    public GameStateType getNextState();

    public void deleteGame(Integer gameId);

    public Game joinGame(Integer gameId, String username, String token);

    public Game removePlayer(Integer gameId, String username, String token);

    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token);

    public void buyMarketCard(Integer marketCardIngameId, String token);

    public Game startGame(Integer gameId);

    public void finishGame(Game game);
}
