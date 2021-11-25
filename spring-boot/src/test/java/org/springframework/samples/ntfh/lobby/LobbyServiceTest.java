package org.springframework.samples.ntfh.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.samples.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.samples.ntfh.lobby.LobbyRepository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class LobbyServiceTest {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserService userService;
    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void testCountWithInitialData() {
        Integer count = lobbyService.lobbyCount();
        assertEquals(2, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(lobbyService.findAll()).size();
        assertEquals(2, count);
    }

    @Test
    public void testFindById() {
        Lobby tester = this.lobbyService.findLobbyById(1).orElse(null);
        assertEquals("test lobby 1", tester.getName());
        assertEquals(1, tester.getGame().getId());
        assertEquals(false, tester.getHasScenes());
        assertEquals(true, tester.getSpectatorsAllowed());
        assertEquals(2, tester.getMaxPlayers());
        assertEquals(userService.findUser("andres").get(), tester.getHost());
        assertEquals(userService.findUser("andres").get(), tester.getLeader());
    }

    @Test
    public void testFindByIdNonSensitive() {
        Lobby tester = this.lobbyService.findLobbyByIdNonSensitive(1).orElse(null);
        assertEquals("test lobby 1", tester.getName());
        assertEquals(null, tester.getGame());
        assertEquals(false, tester.getHasScenes());
        assertEquals(true, tester.getSpectatorsAllowed());
        assertEquals(2, tester.getMaxPlayers());
    }

    @Test
    public void testSave() {
        Lobby tester = new Lobby();
        tester.setName("testSave");
        tester.setHasScenes(true);
        tester.setSpectatorsAllowed(false);
        tester.setMaxPlayers(4);
        tester.setHost(userService.findUser("andres").get());
        lobbyService.save(tester);
        assertEquals(3, lobbyService.findLobbyById(tester.getId()).get().getId());
    }

    @Test
    public void testDeleteLobby() {
        Lobby tester = lobbyService.findLobbyById(1).get();
        lobbyService.deleteLobby(tester);
        assertEquals(null, lobbyService.findLobbyById(tester.getId()).orElse(null));
    }

//TODO
    @Disabled
    @Test
    public void testJoinToLobby() {
        Integer lobbyTesterId = lobbyService.findLobbyById(1).get().getId();
        User requester = userService.findUser("alex").get();
        String requesterString = requester.getUsername();
        String reqToken = TokenUtils.generateJWTToken(requester);
        lobbyService.joinLobby(lobbyTesterId, requesterString, reqToken);
        assertEquals(true, lobbyService.findLobbyById(1).get().getUsers().contains(requester));
    }

    @Test
    public void testH14E1(){
        List<Lobby> lobbiesServiceList= new ArrayList<>();
        lobbyService.findAll().forEach(x->lobbiesServiceList.add(x));
        Integer playersLobby1Sevice=lobbiesServiceList.get(0).getUsers().size();
        
        List<Lobby> lobbiesRepositoryList = new ArrayList<>();
        lobbyRepository.findAll().forEach(x->lobbiesRepositoryList.add(x));
        Integer playersLobby1Repo=lobbiesRepositoryList.get(0).getUsers().size();
        
        assertEquals(playersLobby1Repo, playersLobby1Sevice);
    }


    
}
