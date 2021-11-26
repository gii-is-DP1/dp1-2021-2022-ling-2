package org.springframework.samples.ntfh.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.samples.ntfh.character.Character;
import org.springframework.samples.ntfh.character.CharacterService;
import org.springframework.samples.ntfh.user.UserService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected CharacterService characterService;

    private Player playerTester;

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(playerService.findAll()).size();
        assertEquals(8, count);
    }

    // @Test
    // public void testFindByUserId() {
    //     Player tester = this.playerService.findPlayer(1).get();
    //     assertEquals("pablo", tester.getUser().getUsername());
    //     assertEquals(0, tester.getGlory());
    // }

    @Disabled
    @Test
    public void testSavePlayer() {
        Character character5 = characterService.findCharacterById(5).get();
        Player tester = new Player();
        tester.setId(3);
        tester.setGlory(1);
        tester.setGold(1);
        tester.setKills(1);
        tester.setWounds(1);
        tester.setCharacterType(character5);
        tester.setUser(userService.findUser("merlin").get());
        playerService.savePlayer(tester);
        // assertEquals(3, playerService.findPlayer(tester.getId()).get());
        // playerRepository.deleteById(3);
    }
    
}
