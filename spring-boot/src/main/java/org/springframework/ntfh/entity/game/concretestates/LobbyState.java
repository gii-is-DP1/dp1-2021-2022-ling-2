package org.springframework.ntfh.entity.game.concretestates;

import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameState;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.ntfh.util.State;

@State
public class LobbyState implements GameState {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Override
    public void preState(Game game) {
        // Nothing happens in this state, but is util in the others
    }

    @Override
    public GameStateType getNextState() {
        return GameStateType.ONGOING;
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
    public Game joinGame(Game game, User user) {
        if (game.getPlayers().stream().anyMatch(p -> p.getUser().equals(user))) {
            throw new IllegalArgumentException("The player is already in the lobby") {};
        }

        if (game.getMaxPlayers().equals(game.getPlayers().size()))
            throw new MaximumLobbyCapacityException("The lobby is full") {};

        Player player = playerService.createPlayer(user);

        if (game.getPlayers().isEmpty()) {
            // The first player to join will be the host/leader
            game.setLeader(player);
        }

        game.getPlayers().add(player);
        player.setGame(game);
        player.getUser().setPlayer(player);
        return gameService.save(game);
    }

    @Override
    public Game removePlayer(Integer gameId, String username) {
        Game game = gameService.findGameById(gameId);
        User user = userService.findUser(username);
        Player player = user.getPlayer();
        player.setGame(null);
        game.getPlayers().remove(player);
        return gameService.save(game);
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {
        throw new IllegalStateException("You can't play cards in the lobby");
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        throw new IllegalStateException("You can't buy cards in the lobby");
    }

    @Override
    public Game startGame(Integer gameId) {
        Game game = gameService.findGameById(gameId);
        game.setStartTime(Timestamp.from(Instant.now()));
        gameService.setNextState(game);
        return gameService.save(game);
    }

    @Override
    public void finishGame(Game game) {
        throw new IllegalStateException("You can't finish a game that has not started");

    }

}
