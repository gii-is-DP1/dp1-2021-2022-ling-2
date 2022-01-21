package org.springframework.ntfh.configuration;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
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
@Transactional
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

    private List<User> usersFree = new ArrayList<>(); // Users who are not taken
    private List<User> usersInLobby = new ArrayList<>(); // Users currently in a lobby
    private List<User> usersInGame = new ArrayList<>(); // Users currently in a game

    @Override
    public void run(String... args) throws Exception {
        random = new Random();
        loadInitialData();
    }

    private void loadInitialData() {
        // If this number is not enough, additional users will be created automatically
        populateUsers(25);

        createGamesInFinishedState(30);
        createGamesInGameState(4);
        createGamesInLobbyState(2);
    }

    private void populateUsers(Integer n) {
        // Fill a user bank with the number of users specified. Initially they are all "in the menu" and ready to join
        // games
        usersFree = IntStream.range(1, n + 1).boxed().map(i -> createTestUser()).collect(Collectors.toList());
    }

    /**
     * Example. if games=100 and users=4, there will be 100 games played with variations of these users, so these 4 will
     * have rich stats about wins, glory, kills, gold...
     * 
     * @author andrsdt
     * @param numberOfGames number of games to create and finish
     * @param numberOfUsers number of users that will play those games
     */
    private void createGamesInFinishedState(Integer numberOfGames) {
        IntStream.range(0, numberOfGames).forEach(i -> {
            Game g = createGameInLobbyState();
            g = gameService.startGame(g.getId());

            // Make the duration of the game something between 5 and 100 minutes
            g.setStartTime(Timestamp.from(Instant.now().minus(random.nextInt(100) + 5L, ChronoUnit.MINUTES)));

            g.getPlayers().forEach(p -> {
                User user = p.getUser();
                removeIfContaining(usersInLobby, user);
                usersInGame.add(user);

                // Make sure there is some dynamic data
                p.setGlory(random.nextInt(20));
                p.setKills(random.nextInt(15));
                p.setGold(random.nextInt(10));
                p.setWounds(random.nextInt(p.getCharacter().getBaseHealth() + 1));

                removeIfContaining(usersInGame, user);
                usersFree.add(user);
            });
            g = gameService.finishGame(g);
        });
    }

    private void createGamesInGameState(Integer numberOfGames) {
        IntStream.range(0, numberOfGames).forEach(i -> {
            Game g = createGameInLobbyState();
            g.getPlayers().forEach(p -> {
                User user = p.getUser();
                removeIfContaining(usersInLobby, user);
                usersInGame.add(user);
            });
            gameService.startGame(g.getId());
        });
    }

    private void createGamesInLobbyState(Integer numberOfGames) {
        IntStream.range(0, numberOfGames).forEach(i -> createGameInLobbyState());
    }

    private Game createGameInLobbyState() {
        Game lobby = createRandomLobby();
        // For every lobby, create users to fill them with players
        for (int j = 0; j < lobby.getMaxPlayers(); j++) {
            // Take a random user who is not playing a game
            if (usersFree.isEmpty())
                usersFree.add(createTestUser());
            User testUser = usersFree.get(random.nextInt(usersFree.size()));

            gameService.joinGame(lobby, testUser);

            removeIfContaining(usersFree, testUser);
            usersInLobby.add(testUser);

            Player player = testUser.getPlayer();

            List<CharacterTypeEnum> charactersChosen = lobby.getPlayers().stream().filter(p -> p.getCharacter() != null)
                    .map(p -> p.getCharacter().getCharacterTypeEnum()).collect(Collectors.toList());

            // Ensure there are no repeated characters
            Character randomCharacter = null;
            do {
                randomCharacter = characterService.findById(random.nextInt(8) + 1);
            } while (charactersChosen.contains(randomCharacter.getCharacterTypeEnum()));

            player.setCharacter(randomCharacter);
            playerService.savePlayer(player);
        }
        return lobby;
    }

    private Game createLobby(String name, Boolean hasScenes, Boolean spectatorsAllowed, Integer maxPlayers) {
        Game game = new Game();
        game.setName(name);
        game.setHasScenes(hasScenes);
        game.setSpectatorsAllowed(spectatorsAllowed);
        game.setMaxPlayers(maxPlayers);
        return gameService.createGame(game);
    }

    private User createTestUser() {
        User user = new User();
        String username = "dummy" + testUserCount;
        user.setUsername(username);
        user.setEmail(username + "@mail.com");
        user.setPassword(username);
        testUserCount++;
        return userService.createUser(user);
    }

    private Game createRandomLobby() {
        Boolean hasScenes = random.nextBoolean();
        Boolean spectatorsAllowed = random.nextBoolean();
        Integer maxPlayers = random.nextInt(3) + 2; // Random between 2 and 4
        Game lobby = createLobby("Test lobby " + testLobbyCount, hasScenes, spectatorsAllowed, maxPlayers);
        testLobbyCount++;
        return lobby;
    }

    /**
     * this aux method is necessary because the common list.remove does not work since the pointer in memory to the
     * player changes when retrieved from the database
     */
    private void removeIfContaining(List<User> list, User user) {
        list.stream().filter(u -> u.getId().equals(user.getId())).findAny().ifPresent(list::remove);
    }
}
