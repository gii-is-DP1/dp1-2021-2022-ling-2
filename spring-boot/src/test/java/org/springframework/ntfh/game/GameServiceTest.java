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
        User alex = userService.findUser("alex").get();
        User pablo = userService.findUser("pablo").get();
        Set<User> users = new HashSet<User>();
        users.add(alex);
        users.add(pablo);

        lobbyTester = new Lobby();
        lobbyTester.setName("Test lobby");
        lobbyTester.setHasScenes(true);
        lobbyTester.setSpectatorsAllowed(false);
        lobbyTester.setMaxPlayers(4);
        lobbyTester.setUsers(users);
        lobbyTester.setHost(alex);
        lobbyTester.setLeader(alex);
        lobbyService.save(lobbyTester);

        alex.setCharacter(characterService.findCharacterById(2).get());
        pablo.setCharacter(characterService.findCharacterById(4).get());
        Player alexPlayer = playerService.createFromUser(alex, lobbyTester, 0);
        Player pabloPlayer = playerService.createFromUser(pablo, lobbyTester, 1);
        List<Player> players = Lists.newArrayList(alexPlayer, pabloPlayer);

        gameTester = new Game();
        gameTester.setStartTime(System.currentTimeMillis());
        gameTester.setHasScenes(true);
        gameTester.setPlayers(players);
        gameTester.setLeader(players.get(0));
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
        assertEquals(gameRepository.findById(tester.getId()).get().getId(), tester.getId());
    }

    @Test
    public void testCreateFromLobbyNotEnoughPlayers() {
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
