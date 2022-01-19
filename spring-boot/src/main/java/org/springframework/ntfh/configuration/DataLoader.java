package org.springframework.ntfh.configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.stereotype.Component;

/** @see https://stackoverflow.com/a/61019382/12169711 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CharacterService characterService;

    @Override
    public void run(String... args) throws Exception {
        loadInitialData();
    }

    private void loadInitialData() {
        // Create 30 test users
        List<User> testUsers = IntStream.range(1, 30).boxed().map(i -> createUser("testUser" + i))
                .collect(Collectors.toList());

        // Create 4 games and fill them with users
        createLobbyWithUsersAndStartGame("Test lobby 1", true, false, 2,
                List.of(testUsers.get(0), testUsers.get(1)));
        createLobbyWithUsersAndStartGame("Test lobby 2", false, true, 3,
                List.of(testUsers.get(2), testUsers.get(3), testUsers.get(4)));
        createLobbyWithUsersAndStartGame("Test lobby 3", false, false, 4,
                List.of(testUsers.get(5), testUsers.get(6), testUsers.get(7), testUsers.get(8)));
        createLobbyWithUsersAndStartGame("Test lobby 4", true, true, 4,
                List.of(testUsers.get(9), testUsers.get(10), testUsers.get(11), testUsers.get(12)));
    }

    private void createLobbyWithUsersAndStartGame(String name, Boolean hasScenes,
            Boolean spectatorsAllowed, Integer maxPlayers, List<User> users) {
        Game lobby = createLobby(name, hasScenes, spectatorsAllowed, maxPlayers);
        users.forEach(user -> {
            gameService.joinGame(lobby, user);
            Player player = user.getPlayer();
            Character randomCharacter = characterService.findById(player.getId() % 8 + 1);
            player.setCharacter(randomCharacter);
            playerService.savePlayer(player);
        });
        gameService.startGame(lobby.getId()); // TODO remove getId() when alex and pablo refactor it
    }

    private Game createLobby(String name, Boolean hasScenes, Boolean spectatorsAllowed,
            Integer maxPlayers) {
        Game game = new Game();
        game.setName(name);
        game.setHasScenes(hasScenes);
        game.setSpectatorsAllowed(spectatorsAllowed);
        game.setMaxPlayers(maxPlayers);
        return gameService.createGame(game);
    }

    private User createUser(String name) {
        User user = new User();
        user.setUsername(name);
        user.setEmail(name + "@mail.com");
        user.setPassword(name);
        return userService.createUser(user);
    }
}
