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

        createUsersWithTwoGamesPlayed();
    }

    private void createUsersWithTwoGamesPlayed() {
        User user1 = createTestUser(testUserCount);
        testUserCount++;

        User user2 = createTestUser(testUserCount);
        testUserCount++;

        User user3 = createTestUser(testUserCount);
        testUserCount++;

        // Create and finish game 1 with User1, User2
        Game game1 = createLobby("testLobby1", true, true, 2);
        gameService.joinGame(game1, user1);
        gameService.joinGame(game1, user2);

        Player player1 = game1.getPlayers().get(0);
        player1.setCharacter(characterService.findById(1));

        Player player2 = game1.getPlayers().get(1);
        player2.setCharacter(characterService.findById(3));

        game1 = gameService.startGame(game1.getId());
        gameService.finishGame(game1);

        // Create and finish game 2 with User1, User3
        Game game2 = createLobby("testLobby2", true, true, 3);
        gameService.joinGame(game2, user1);
        gameService.joinGame(game2, user3);

        Player player3 = game2.getPlayers().get(0);
        player3.setCharacter(characterService.findById(1));

        Player player4 = game2.getPlayers().get(1);
        player4.setCharacter(characterService.findById(3));

        game2 = gameService.startGame(game2.getId());
        gameService.finishGame(game2);
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
