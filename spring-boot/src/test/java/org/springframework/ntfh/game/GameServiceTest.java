package org.springframework.ntfh.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
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
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
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

    protected Game gameTester;

    protected Lobby lobbyTester;

    @BeforeEach
    public void init() {
        Set<User> users = new HashSet<>();
        users.add(userService.findUser("alex").get());
        users.add(userService.findUser("pablo").get());

        lobbyTester = new Lobby();
        lobbyTester.setName("init");
        lobbyTester.setHasScenes(true);
        lobbyTester.setSpectatorsAllowed(false);
        lobbyTester.setMaxPlayers(4);
        lobbyTester.setUsers(users);
        lobbyTester.setHost(userService.findUser("alex").get());
        lobbyTester.setLeader(userService.findUser("alex").get());
        lobbyService.save(lobbyTester);

        List<Player> players = new ArrayList<>();
        User user = userService.findUser("alex").get();
        User user1 = userService.findUser("pablo").get();
        user.setCharacter(characterService.findCharacterById(2).get());
        user1.setCharacter(characterService.findCharacterById(4).get());
        Player player = playerService.createFromUser(user, lobbyTester, 0);
        Player player1 = playerService.createFromUser(user1, lobbyTester, 0);
        players.add(player);
        players.add(player1);

        gameTester = new Game();
        gameTester.setStartTime(System.currentTimeMillis());
        gameTester.setHasScenes(true);
        gameTester.setPlayers(players);
        gameTester.setLeader(Lists.newArrayList(players).get(0));
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
    public void testfindById() {
        Game tester = this.gameService.findGameById(1);
        assertEquals(true, tester.getHasScenes());
        assertEquals(1, tester.getLeader().getId());
    }

    // TODO edit init() method to create a lobby with 2 users, this test currently
    // fails since the lobby only has 1 user
    // @Disabled
    @Test
    public void testCreatefromLobby() {
        Game tester = gameService.createFromLobby(lobbyTester);
        assertEquals(gameRepository.findById(tester.getId()).get().getId(), tester.getId());
    }

    @Test
    public void testCreatefromLobbyNotEnoughPlayers() {
        lobbyTester.removeUser(userService.findUser("pablo").get());
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
