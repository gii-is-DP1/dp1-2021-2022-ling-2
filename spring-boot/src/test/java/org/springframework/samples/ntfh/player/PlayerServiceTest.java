package org.springframework.samples.ntfh.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.ntfh.character.CharacterService;
import org.springframework.samples.ntfh.entity.lobby.LobbyService;
import org.springframework.samples.ntfh.entity.player.Player;
import org.springframework.samples.ntfh.entity.player.PlayerRepository;
import org.springframework.samples.ntfh.entity.player.PlayerService;
import org.springframework.samples.ntfh.entity.user.User;
import org.springframework.samples.ntfh.entity.user.UserService;
import org.springframework.stereotype.Service;

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
        Player tester2 = new Player();
        tester2.setGlory(1);
        tester2.setGold(4);
        tester2.setKills(5);
        tester2.setWounds(1);
        tester2.setCharacterType(characterService.findCharacterById(5).get());
        tester2.setUser(userService.findUser("merlin").get());
        playerService.savePlayer(tester2);
        assertEquals(tester2.getGold(), playerService.findPlayer(tester2.getId()).get().getGold());
        assertEquals(tester2.getKills(), playerService.findPlayer(tester2.getId()).get().getKills());
        playerRepository.deleteById(tester2.getId());
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