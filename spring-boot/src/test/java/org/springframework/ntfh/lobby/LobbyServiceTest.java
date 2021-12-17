package org.springframework.ntfh.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyRepository;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(BCryptPasswordEncoder.class)
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
        User user1 = userService.findUser("user1").get();
        Set<User> users = Sets.newSet(user1);

        lobbyTester = new Lobby();
        lobbyTester.setName("init");
        lobbyTester.setHasScenes(true);
        lobbyTester.setSpectatorsAllowed(false);
        lobbyTester.setMaxPlayers(4);
        lobbyTester.setUsers(users);
        lobbyTester.setHost(user1);
        lobbyTester.setLeader(user1);
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

    // H7
    // Un user no crea el lobby en si, sino que a traves del botón de crear una
    // partida tiene acceso a la creación, por lo tanto la
    // creación de un lobby es ya de por si este test
    @Test
    public void testSave() {
        // Test made in the init
        // TODO hacer un test q cuente antes y despues de añadir un dato, asi en el
        // assert q compruebe q el
        // contador inicial sea igual al final +1
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
        lobbyTester.addUser(userService.findUser("alex").get());
        Integer numUsersInLobby=lobbyTester.getUsers().size();
        assertEquals(1, numUsersInLobby);
    }

}
