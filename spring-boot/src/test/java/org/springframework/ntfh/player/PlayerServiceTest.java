package org.springframework.ntfh.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 * @author alegestor
 */
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class PlayerServiceTest {

    @Autowired
    protected PlayerService playerService;

    @Autowired
    protected CharacterService characterService;

    @Autowired
    protected UserService userService;

    // Number of players in the DB
    private final Integer INITIAL_COUNT = 0;

    private Player currentPlayer;

    @BeforeEach
    void createPlayer() {
        Player tester = new Player();
        tester.setGlory(1);
        tester.setGold(4);
        tester.setKills(5);
        tester.setCharacter(characterService.findById(7));
        tester.setWounds(1);
        tester.setGuard(0);
        playerService.savePlayer(tester);

        currentPlayer = tester;
    }

    @AfterEach
    void teardown() {
        try {
            playerService.delete(currentPlayer);
        } catch (Exception exception) {}
    }

    @Test
    void testCountWithInitialData() {
        Integer count = playerService.playerCount();

        assertThat(count).isEqualTo(INITIAL_COUNT + 1);
    }

    @Test
    void testSavePlayer() {
        // Player created in the BeforeEach
        Player tester = currentPlayer;
        Integer ORIGINAL_GLORY = 1;
        Integer ORIGINAL_GOLD = 4;
        Integer ORIGINAL_KILLS = 5;
        Integer ORIGINAL_WOUNDS = 1;

        assertThat(tester.getGlory()).isEqualTo(ORIGINAL_GLORY);
        assertThat(tester.getGold()).isEqualTo(ORIGINAL_GOLD);
        assertThat(tester.getKills()).isEqualTo(ORIGINAL_KILLS);
        assertThat(tester.getWounds()).isEqualTo(ORIGINAL_WOUNDS);
        assertThat(tester.getCharacter()).isEqualTo(characterService.findById(7));
    }


    @Test
    void testDelete() {
        playerService.delete(currentPlayer);

        assertThrows(Exception.class, () -> {
            playerService.findById(currentPlayer.getId());
        });
    }

    @Test
    void testDeleteById() {
        playerService.deleteById(currentPlayer.getId());

        assertThrows(Exception.class, () -> {
            playerService.findById(currentPlayer.getId());
        });
    }

    @Test
    void testfindAll() {
        Integer count = Lists.newArrayList(playerService.findAll()).size();

        assertThat(count).isEqualTo(INITIAL_COUNT + 1);
    }

    @Test
    void testFindByPlayerId() {
        Player tester = this.playerService.findById(currentPlayer.getId());
        Integer ORIGINAL_GLORY = 1;
        Integer ORIGINAL_GOLD = 4;
        Integer ORIGINAL_KILLS = 5;
        Integer ORIGINAL_WOUNDS = 1;

        assertThat(tester.getGlory()).isEqualTo(ORIGINAL_GLORY);
        assertThat(tester.getGold()).isEqualTo(ORIGINAL_GOLD);
        assertThat(tester.getKills()).isEqualTo(ORIGINAL_KILLS);
        assertThat(tester.getWounds()).isEqualTo(ORIGINAL_WOUNDS);
        assertThat(tester.getCharacter()).isEqualTo(characterService.findById(7));
    }

    @Test
    @Disabled
    void testCreatePlayer() {
        User user = userService.findUser("user4");
        Player tester = playerService.createPlayer(user);
        assertThat(tester.getGold()).isZero();
    }

}
