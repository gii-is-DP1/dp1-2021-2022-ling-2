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
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LobbyState implements GameState {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Override
    public void preState(Game game) {
        //
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
    public Game joinGame(Integer gameId, String username, String token) {
        // TODO "game" here is redundant, can't we just pass it from the gameService?
        Game game = gameService.findGameById(gameId);
        if (game.getPlayers().stream().anyMatch(p -> p.getUser().getUsername().equals(username))) {
            throw new IllegalArgumentException("The player is already in the lobby") {};
        }

        if (game.getMaxPlayers().equals(game.getPlayers().size()))
            throw new MaximumLobbyCapacityException("The lobby is full") {};

        String usernameFromToken = TokenUtils.usernameFromToken(token);
        if (!username.equals(usernameFromToken))
            throw new IllegalArgumentException("The Token username and the request one do not coincide") {};

        // TODO get this via a converter before the controller
        User user = userService.findUser(username);

        // ! TODO adapt this to new model
        // user.setLobby(lobby);
        // user.setCharacter(null);
        Player player = playerService.createPlayer(user); // TODO createPlayer(user);

        if (game.getPlayers().isEmpty()) {
            // The first player to join will be the host/leader
            game.setLeader(player);
        }

        game.getPlayers().add(player);
        player.setGame(game); // TODO redundant? we have mappedBy above
        player.getUser().setPlayer(player);
        return game;
    }

    @Override
    public Game removePlayer(Integer gameId, String username, String token) {
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
