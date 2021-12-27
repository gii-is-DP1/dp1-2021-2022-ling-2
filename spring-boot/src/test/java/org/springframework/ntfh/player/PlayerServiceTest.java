package org.springframework.ntfh.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 * @author alegestor
 */

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(BCryptPasswordEncoder.class)
public class PlayerServiceTest {

    @Autowired
    protected PlayerService playerService;

    @Autowired
    protected CharacterService characterService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected LobbyService lobbyService;

    // Number of players in the DB
    private final Integer INITIAL_COUNT = 8;

    private Player currentPlayer;

    @BeforeEach
    void createPlayer() {
        Player tester = new Player();
        tester.setGlory(1);
        tester.setGold(4);
        tester.setKills(5);
        tester.setWounds(1);
        tester.setTurnOrder(2);
        tester.setCharacterType(characterService.findCharacterById(7).get());
        tester.setUser(userService.findUser("merlin").get());
        playerService.savePlayer(tester);

        currentPlayer = tester;
    }

    @AfterEach
    void deletePlayer() {
        playerService.delete(currentPlayer);
    }

    @Test
    public void testCountWithInitialData() {
        Integer count = playerService.playerCount();
        assertEquals(INITIAL_COUNT + 1, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(playerService.findAll()).size();
        assertEquals(INITIAL_COUNT + 1, count);
    }

    @Test
    public void testFindByPlayerId() {
        Player tester = this.playerService.findById(currentPlayer.getId());
        assertEquals("merlin", tester.getUser().getUsername());
        assertEquals(1, tester.getGlory());
        assertEquals(4, tester.getGold());
        assertEquals(5, tester.getKills());
        assertEquals(2, tester.getTurnOrder());
        assertEquals(1, tester.getWounds());
        assertEquals(characterService.findCharacterById(7).get(), tester.getCharacterType());
    }

    @Test
    public void testSavePlayer() {
        // Player created in the BeforeEach
        Player tester = currentPlayer;
        assertEquals("merlin", tester.getUser().getUsername());
        assertEquals(1, tester.getGlory());
        assertEquals(4, tester.getGold());
        assertEquals(5, tester.getKills());
        assertEquals(2, tester.getTurnOrder());
        assertEquals(1, tester.getWounds());
        assertEquals(characterService.findCharacterById(7).get(), tester.getCharacterType());
    }

    @Test
    public void testCreateFromUser() {
        User user = userService.findUser("user4").get();
        user.setCharacter(characterService.findCharacterById(2).get());
        Lobby lobby = lobbyService.findLobby(3);
        Player tester = playerService.createFromUser(user, lobby, 3);
        assertEquals("user4", tester.getUser().getUsername());
    }

}