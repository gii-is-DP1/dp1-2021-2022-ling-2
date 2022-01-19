package org.springframework.ntfh.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
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

    private Random random;

    private Integer testUserCount = 1;

    private Integer testLobbyCount = 1;

    @Override
    public void run(String... args) throws Exception {
        random = new Random();
        loadInitialData();
    }

    private void loadInitialData() {
        createGamesInLobbyState(2);
        createGamesInGameState(3);
        createGamesInFinishedState(4);
    }

    private void createGamesInFinishedState(Integer number) {
        createGamesInLobbyState(number).forEach(g -> {
            g = gameService.startGame(g.getId());
            g = gameService.finishGame(g);
        });
    }

    private void createGamesInGameState(Integer number) {
        createGamesInLobbyState(number).forEach(g -> gameService.startGame(g.getId()));
    }

    private List<Game> createGamesInLobbyState(Integer number) {

        List<Game> games = new ArrayList<>();

        // Create as many lobbies as requested in number parameter
        for (int i = 0; i < number; i++) {
            Boolean hasScenes = random.nextBoolean();
            Boolean spectatorsAllowed = random.nextBoolean();
            Integer maxPlayers = random.nextInt(2) + 2; // Random between 2 and 4
            Game lobby = createLobby("Test lobby " + testLobbyCount, hasScenes, spectatorsAllowed, maxPlayers);
            testLobbyCount++;

            // For every lobby, create users to fill them with players
            for (int j = 0; j < maxPlayers; j++) {
                User testUser = createTestUser(testUserCount);
                gameService.joinGame(lobby, testUser);
                Player player = testUser.getPlayer();

                List<CharacterTypeEnum> charactersChosen =
                        lobby.getPlayers().stream().filter(p -> p.getCharacter() != null)
                                .map(p -> p.getCharacter().getCharacterTypeEnum()).collect(Collectors.toList());

                // Ensure there are no repeated characters
                Character randomCharacter = null;
                do {
                    randomCharacter = characterService.findById(random.nextInt(8) + 1);
                } while (charactersChosen.contains(randomCharacter.getCharacterTypeEnum()));

                player.setCharacter(randomCharacter);
                playerService.savePlayer(player);
            }
            games.add(lobby);
        }
        return games;
    }

    private Game createLobby(String name, Boolean hasScenes, Boolean spectatorsAllowed, Integer maxPlayers) {
        Game game = new Game();
        game.setName(name);
        game.setHasScenes(hasScenes);
        game.setSpectatorsAllowed(spectatorsAllowed);
        game.setMaxPlayers(maxPlayers);
        return gameService.createGame(game);
    }

    private User createTestUser(Integer number) {
        User user = new User();
        String username = "TestUser" + number;
        user.setUsername(username);
        user.setEmail(username + "@mail.com");
        user.setPassword(username);
        testUserCount++;
        return userService.createUser(user);
    }
}
