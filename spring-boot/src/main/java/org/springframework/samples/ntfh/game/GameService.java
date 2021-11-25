package org.springframework.samples.ntfh.game;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.ntfh.lobby.Lobby;
import org.springframework.samples.ntfh.lobby.LobbyService;
import org.springframework.samples.ntfh.player.Player;
import org.springframework.samples.ntfh.player.PlayerService;
import org.springframework.samples.ntfh.user.User;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LobbyService lobbyService;

    @Transactional
    public Integer gameCount() {
        return (int) gameRepo.count();
    }

    public Iterable<Game> findAll() {
        return gameRepo.findAll();
    }

    @Transactional
    public Optional<Game> findGameById(int id) {
        return gameRepo.findById(id);
    }

    @Transactional
    public Game createFromLobby(@Valid Lobby lobby) {
        Game game = new Game();
        game.setStartTime(System.currentTimeMillis());
        game.setHasScenes(lobby.getHasScenes());

        Set<User> users = lobby.getUsers();
        // TODO see implement this and see how are we going to do it
        Set<Player> players = users.stream().map(user -> playerService.createFromUser(user, lobby))
                .collect(Collectors.toSet());
        game.setPlayers(players);
        game.setLeader(players.iterator().next()); // Random leader

        Game savedGame = gameRepo.save(game);

        // Once the game is in the database, we update the lobby with a FK to it
        lobby.setGame(game);
        lobbyService.save(lobby);

        return savedGame;
    }

    @Transactional
    public Game save(@Valid Game game) {
        // Return the game created after saving it
        return gameRepo.save(game);
    }

    public void delete(Game game) {
        gameRepo.delete(game);
    }

}
