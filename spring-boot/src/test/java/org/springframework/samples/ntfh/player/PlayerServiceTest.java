package org.springframework.samples.ntfh.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.samples.ntfh.character.CharacterService;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.lobby.LobbyService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected CharacterService characterService;


    @Test
    public void testCountWithInitialData() {
        Integer count = playerService.playerCount();
        assertEquals(8, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(playerService.findAll()).size();
        assertEquals(8, count);
    }

    @Test
    public void testFindByPlayerId() {
        Player tester = this.playerService.findPlayer(1).get();
        assertEquals("pablo", tester.getUser().getUsername());
        assertEquals(0, tester.getGlory());
    }

    @Test
    public void testSavePlayer() {
        Player tester = new Player();
        tester.setGlory(1);
        tester.setGold(1);
        tester.setKills(1);
        tester.setWounds(1);
        tester.setCharacterType(characterService.findCharacterById(5).get());
        tester.setUser(userService.findUser("merlin").get());
        playerService.savePlayer(tester);
        //assertEquals(1, playerService.findPlayer(tester.getId()).get().getGlory());
        //assertEquals(9, playerService.findPlayer(tester.getId()).get().getId());
        assertEquals(9, playerService.findPlayer(tester.getId()).get().getId());
        playerRepository.deleteById(tester.getId());
    }

    @Test
    public void testCreateFromUser() {
        User user = userService.findUser("ezio").get();
        user.setCharacter(characterService.findCharacterById(5).get());
        Player tester = playerService.createFromUser(user, lobbyService.findLobbyById(3).get());
        assertEquals("ezio", playerService.findPlayer(tester.getId()).get().getUser().getUsername());
        playerRepository.deleteById(tester.getId());
        userService.findUser("ezio").get().setCharacter(null);
    }
    
}