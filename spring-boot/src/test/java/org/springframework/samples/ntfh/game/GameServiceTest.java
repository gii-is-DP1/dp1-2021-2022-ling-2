package org.springframework.samples.ntfh.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tomcat.jni.Time;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.ntfh.lobby.Lobby;
import org.springframework.samples.ntfh.lobby.LobbyService;
import org.springframework.samples.ntfh.player.Player;
import org.springframework.samples.ntfh.player.PlayerService;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.samples.ntfh.util.TokenUtils;
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

    protected Game gameTester;

    protected Lobby lobbyTester;

    // @BeforeEach
    // public void init() {
    //     Set<User> users = new HashSet<>();
    //     users.add(userService.findUser("alex").get());

    //     lobbyTester = new Lobby();
    //     lobbyTester.setName("init");
    //     lobbyTester.setHasScenes(true);
    //     lobbyTester.setSpectatorsAllowed(false);
    //     lobbyTester.setMaxPlayers(4);
    //     lobbyTester.setUsers(users);
    //     lobbyTester.setHost(userService.findUser("alex").get());
    //     lobbyTester.setLeader(userService.findUser("alex").get());
    //     lobbyService.save(lobbyTester);

    //     Set<Player> players = new HashSet<>();
    //     User user = userService.findUser("alex").get();
    //     Player player = playerService.createFromUser(user, lobbyTester);
    //     players.add(player);

    //     gameTester = new Game();
    //     gameTester.setStartTime(Long.valueOf(1637854607));
    //     gameTester.setHasScenes(true);
    //     gameTester.setPlayers(players);
    //     gameTester.setLeader(Lists.newArrayList(players).get(0));
    // }

    // @AfterEach
    // public void teardown() {
    //     if (!gameService.findGameById(gameTester.getId()).equals(null)) gameService.delete(gameTester);
    // }

    @Test
    public void testCountWithInitialData() {
        Integer count = gameService.gameCount();
        assertEquals(3, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(gameService.findAll()).size();
        assertEquals(3, count);
    }

    @Test
    //H1
    public void testfindAllListVersion() {

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

    @Disabled
    @Test
    public void testCreatefromLobby() {
        Lobby lobbyTest = lobbyService.findLobbyById(1).get();

        Game tester = this.gameService.createFromLobby(lobbyTest);
        
        Integer lobbyTesterId = lobbyService.findLobbyById(1).get().getId();
        User requester = userService.findUser("alex").get();
        String requesterString = requester.getUsername();
        String reqToken = TokenUtils.generateJWTToken(requester);
        lobbyService.joinLobby(lobbyTesterId, requesterString, reqToken);

        assertEquals(3, tester.getId());
    }



}
