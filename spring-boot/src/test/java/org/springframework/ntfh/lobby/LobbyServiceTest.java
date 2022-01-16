package org.springframework.ntfh.lobby;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyRepository;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class LobbyServiceTest {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @Autowired
    private LobbyRepository lobbyRepository;

    private Lobby lobbyTester;

    private Integer INITIAL_LOBBY_COUNT = 3;

    @BeforeEach
    public void init() {
        User user1 = userService.findUser("user1");
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
        try {
            lobbyRepository.delete(lobbyTester);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Test
    public void testCountWithInitialData() {
        Integer count = lobbyService.lobbyCount();
        assertThat(count).isEqualTo(INITIAL_LOBBY_COUNT+1);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(lobbyService.findAll()).size();
        assertThat(count).isEqualTo(INITIAL_LOBBY_COUNT+1);
    }

    @Test
    public void testFindById() {
        Lobby tester = this.lobbyService.findById(1);
        assertThat(tester.getName()).isEqualTo("andres with pablo");
        assertThat(tester.getGame().getId()).isEqualTo(1);
        assertThat(tester.getHasScenes()).isTrue();
        assertThat(tester.getSpectatorsAllowed()).isTrue();
        assertThat(tester.getMaxPlayers()).isEqualTo(2);
        assertThat(tester.getHost()).isEqualTo(userService.findUser("andres"));
        assertThat(tester.getLeader()).isEqualTo(userService.findUser("andres"));
    }

    @Test
    void testFindLobby() {
        Lobby tester = this.lobbyService.findLobby(1);
        assertThat(tester.getName()).isEqualTo("andres with pablo");
        assertThat(tester.getGame().getId()).isEqualTo(1);
        assertThat(tester.getHasScenes()).isTrue();
        assertThat(tester.getSpectatorsAllowed()).isTrue();
        assertThat(tester.getMaxPlayers()).isEqualTo(2);
        assertThat(tester.getHost()).isEqualTo(userService.findUser("andres"));
        assertThat(tester.getLeader()).isEqualTo(userService.findUser("andres"));
    }

    @Test
    public void testDeleteLobby() {
        Integer lobbyId = lobbyTester.getId();
        lobbyService.deleteLobby(lobbyTester);
        assertThrows(DataAccessException.class, () -> lobbyService.findById(lobbyId));
    }

    @Test
    public void testRemoveUserFromLobby() {
        Integer lobbyTesterId = lobbyTester.getId();
        User requester = userService.findUser("merlin");
        String requesterString = requester.getUsername();
        String reqToken = TokenUtils.generateJWTToken(requester);
        lobbyService.joinLobby(lobbyTesterId, requesterString, reqToken);
        lobbyService.removeUserFromLobby(lobbyTester, requesterString);
        assertThat(lobbyTester.getUsers().contains(requester)).isFalse();
    }

    @Test
    void testUpdateLobby() {
        lobbyTester.setName("name");
        lobbyService.updateLobby(lobbyTester);
        assertThat(lobbyTester.getName()).isEqualTo("name");
    }

    // H7
    // Un user no crea el lobby en si, sino que a traves del botón de crear una
    // partida tiene acceso a la creación, por lo tanto la
    // creación de un lobby es ya de por si este test
    @Test
    public void testSave() {
        // Test made in the init
        assertThat(lobbyTester.getId()).isEqualTo(lobbyService.findById(lobbyTester.getId()).getId());
        Integer count = Lists.newArrayList(lobbyService.findAll()).size();
        assertThat(count).isEqualTo(INITIAL_LOBBY_COUNT+1);
    }

    // H8 + E1
    @Test
    public void testJoinToLobby_Success() {
        Integer lobbyTesterId = lobbyTester.getId();
        User requester = userService.findUser("merlin");
        String requesterString = requester.getUsername();
        String reqToken = TokenUtils.generateJWTToken(requester);
        lobbyService.joinLobby(lobbyTesterId, requesterString, reqToken);
        assertThat(lobbyService.findById(lobbyTesterId).getUsers().contains(requester)).isTrue();
    }

    // H8 - E1
    @Test
    public void testJoinToLobby_Failure() {
        Lobby fullLobby = lobbyService.findLobby(2);
        Integer fullLobbyId = fullLobby.getId();
        User requester = userService.findUser("ezio");
        String requesterString = requester.getUsername();
        String reqToken = TokenUtils.generateJWTToken(requester);

        assertThrows(MaximumLobbyCapacityException.class, () -> {lobbyService.joinLobby(fullLobbyId, requesterString, reqToken);});
    }

    // H14 + E1
    @Test
    public void testGetNumberOfPlayersInLobby() {
        User user1 = userService.findUser("user1");
        lobbyTester.addUser(user1);
        Integer numUsersInLobby = lobbyTester.getUsers().size();
        assertThat(numUsersInLobby).isEqualTo(1);
    }

}
