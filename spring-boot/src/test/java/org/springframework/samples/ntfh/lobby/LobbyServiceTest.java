package org.springframework.samples.ntfh.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.ntfh.game.Game;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.samples.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class LobbyServiceTest {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @Autowired
    private LobbyRepository lobbyRepository;

    private Lobby lobbyTester;

    @BeforeEach
    public void init() {
        Set<User> users = new HashSet<>();
        users.add(userService.findUser("alex").get());

        lobbyTester = new Lobby();
        lobbyTester.setName("init");
        lobbyTester.setHasScenes(true);
        lobbyTester.setSpectatorsAllowed(false);
        lobbyTester.setMaxPlayers(4);
        lobbyTester.setUsers(users);
        lobbyTester.setHost(userService.findUser("alex").get());
        lobbyTester.setLeader(userService.findUser("alex").get());
        lobbyService.save(lobbyTester);
    }

    @AfterEach
    public void teardown() {
        if (lobbyService.findLobbyById(lobbyTester.getId()).isPresent())
            lobbyService.deleteLobby(lobbyTester);
    }

    @Test
    public void testCountWithInitialData() {
        Integer count = lobbyService.lobbyCount();
        assertEquals(4, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(lobbyService.findAll()).size();
        assertEquals(4, count);
    }

    @Test
    public void testFindById() {
        Lobby tester = this.lobbyService.findLobbyById(1).orElse(null);
        assertEquals("andres with pablo", tester.getName());
        assertEquals(1, tester.getGame().getId());
        assertEquals(true, tester.getHasScenes());
        assertEquals(true, tester.getSpectatorsAllowed());
        assertEquals(2, tester.getMaxPlayers());
        assertEquals(userService.findUser("andres").get(), tester.getHost());
        assertEquals(userService.findUser("andres").get(), tester.getLeader());
    }

    @Test
    public void testFindByIdNonSensitive() {
        Lobby tester = this.lobbyService.findLobbyByIdNonSensitive(1).orElse(null);
        assertEquals("andres with pablo", tester.getName());
        assertEquals(null, tester.getGame());
        assertEquals(true, tester.getHasScenes());
        assertEquals(true, tester.getSpectatorsAllowed());
        assertEquals(2, tester.getMaxPlayers());
    }

  // H7
  // Un user no crea el lobby en si, sino que a traves del botón de crear una partida tiene acceso a la creación, por lo tanto la 
  // creación de un lobby es ya de por si este test
    @Test
    public void testSave() {
//      Test made in the init
        assertEquals(lobbyRepository.findById(lobbyTester.getId()).get().getId(), lobbyTester.getId());

    }
    @Test 
    public void testDeleteLobby() {
        lobbyService.deleteLobby(lobbyTester);
        assertEquals(null, lobbyService.findLobbyById(lobbyTester.getId()).orElse(null));
    }

    @Test
    public void testJoinToLobby() {
        Integer lobbyTesterId = lobbyTester.getId();
        User requester = userService.findUser("merlin").get();
        String requesterString = requester.getUsername();
        String reqToken = TokenUtils.generateJWTToken(requester);
        lobbyService.joinLobby(lobbyTesterId, requesterString, reqToken);
        assertEquals(true, lobbyService.findLobbyById(lobbyTesterId).get().getUsers().contains(requester));
    }

    @Test
    public void testRemoveUserFromLobby() {  
        Integer lobbyTesterId = lobbyTester.getId();
        User requester = userService.findUser("merlin").get();
        String requesterString = requester.getUsername();
        String reqToken = TokenUtils.generateJWTToken(requester);
        lobbyService.joinLobby(lobbyTesterId, requesterString, reqToken);
        lobbyService.removeUserFromLobby(lobbyTester, requesterString);
        assertEquals(false, lobbyTester.getUsers().contains(requester));
    }

    @Test
    public void testH14E1() {
        List<Lobby> lobbiesServiceList = new ArrayList<>();
        lobbyService.findAll().forEach(x -> lobbiesServiceList.add(x));
        Integer playersLobby1Sevice = lobbiesServiceList.get(0).getUsers().size();

        List<Lobby> lobbiesRepositoryList = new ArrayList<>();
        lobbyRepository.findAll().forEach(x -> lobbiesRepositoryList.add(x));
        Integer playersLobby1Repo = lobbiesRepositoryList.get(0).getUsers().size();

        assertEquals(playersLobby1Repo, playersLobby1Sevice);
    }

}
