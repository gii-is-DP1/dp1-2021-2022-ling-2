package org.springframework.samples.ntfh.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.ntfh.lobby.Lobby;
import org.springframework.samples.ntfh.lobby.LobbyService;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.samples.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GameServiceTest {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;

    @Test
    public void testCountWithInitialData() {
        Integer count = gameService.gameCount();
        assertEquals(2, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(gameService.findAll()).size();
        assertEquals(2, count);
    }

    @Test
    public void testfindById() {
        Game tester = this.gameService.findGameById(1).get();
        assertEquals(true, tester.getHasScenes());
        assertEquals(1637854607, tester.getStartTime());
        assertEquals(2, tester.getLeader().getId());
    }

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
