package org.springframework.ntfh.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameRepository;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(BCryptPasswordEncoder.class)
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CharacterService characterService;

    private Game gameTester;

    protected Lobby lobbyTester;

    @BeforeEach
    public void init() {
        User user1 = userService.findUser("user1").get();
        User user2 = userService.findUser("user2").get();
        Set<User> users = Sets.newSet(user1, user2);

        lobbyTester = new Lobby();
        lobbyTester.setName("Init lobby");
        lobbyTester.setHasScenes(true);
        lobbyTester.setSpectatorsAllowed(false);
        lobbyTester.setMaxPlayers(4);
        lobbyTester.setUsers(users);
        lobbyTester.setHost(user1);
        lobbyTester.setLeader(user1);
        lobbyService.save(lobbyTester);

        user1.setCharacter(characterService.findCharacterById(2).get());
        user2.setCharacter(characterService.findCharacterById(4).get());
        Player player1 = playerService.createFromUser(user1, lobbyTester, 0);
        Player player2 = playerService.createFromUser(user1, lobbyTester, 1);
        List<Player> players = Lists.list(player1, player2);

        gameTester = new Game();
        gameTester.setStartTime(System.currentTimeMillis());
        gameTester.setHasScenes(true);
        gameTester.setPlayers(players);
        gameTester.setLeader(player1);
        gameService.save(gameTester);
    }

    @AfterEach
    public void teardown() {
        gameService.delete(gameTester);
    }

    @Test
    public void testCountWithInitialData() {
        Integer count = gameService.gameCount();
        assertEquals(4, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(gameService.findAll()).size();
        assertEquals(4, count);
    }

    @Test
    public void testfindAllListVersion() {
        // H1
        List<Game> gamesServiceList = new ArrayList<>();
        gameService.findAll().forEach(g -> gamesServiceList.add(g));

        List<Game> gamesRepoList = new ArrayList<>();
        gameRepository.findAll().forEach(g -> gamesRepoList.add(g));

        assertEquals(gamesServiceList, gamesRepoList);
    }

    @Test
    public void testFindById() {
        Game tester = this.gameService.findGameById(1);
        assertEquals(true, tester.getHasScenes());
        assertEquals(1, tester.getLeader().getId());
    }

    @Test
    public void testCreateFromLobby() {
        Game tester = gameService.createFromLobby(lobbyTester);
        assertEquals(gameService.findGameById(tester.getId()).getId(), tester.getId());
        gameService.delete(tester);
    }

    @Test
    public void testCreateFromLobbyNotEnoughPlayers() {
        User user2 = userService.findUser("user2").get();
        lobbyTester.removeUser(user2);
        assertThrows(IllegalArgumentException.class, () -> gameService.createFromLobby(lobbyTester));
    }

    @Test
    public void testSaveGame() {
        // Test made in the init
        assertEquals(gameRepository.findById(gameTester.getId()).get().getId(), gameTester.getId());
    }

    @Test
    public void testDeleteGame() {
        gameService.delete(gameTester);
        assertThrows(DataAccessException.class, () -> {
            gameService.findGameById(gameTester.getId());
        });
    }

}
