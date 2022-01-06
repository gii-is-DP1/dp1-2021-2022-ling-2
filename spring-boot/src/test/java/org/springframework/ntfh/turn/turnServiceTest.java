package org.springframework.ntfh.turn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.scene.SceneService;
import org.springframework.ntfh.entity.scene.SceneTypeEnum;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

// TODO Improve the teardown to increase the speed of the test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class turnServiceTest {

    @Autowired
    private TurnService turnService;

    @Autowired
    private GameService gameService;

    @Autowired
    private SceneService sceneService;

    @BeforeEach
    void setup() {
        turnService.initializeFromGame(gameService.findGameById(1));
    }

    @AfterEach
    void teardown() {
        try {
            turnService.delete(turnService.findturnById(1).get().getId());
        } catch(Exception exception) {}
    }

    @Test
    void testCount() {
        Integer counter = turnService.turnCount();
        assertEquals(1, counter);
    }

    @Test
    void testFindAll() {
        Integer counter = Lists.newArrayList(turnService.findAll()).size();
        assertEquals(1, counter);
    }

    @Test
    void testFindTurnById() {
        Turn tester = turnService.findturnById(1).get();
        assertEquals(1, tester.getGame().getId());
    }

    @Test
    void testSaveTurn() {
        Turn testerSaver = turnService.findturnById(1).get();
        testerSaver.setCurrentScene(sceneService.findSceneById(1).get());
        turnService.save(testerSaver);
        assertEquals(SceneTypeEnum.MERCADO_DE_LOTHARION, testerSaver.getCurrentScene().getSceneTypeEnum());
    }

    @Test
    void testDelete() {
        turnService.delete(turnService.findturnById(1).get().getId());
        assertThrows(Exception.class, () -> {turnService.findturnById(1).get();});
    }

    @Test
    void testInitializeFromGame() {
        // Test method made in the init()
        assertEquals(1, turnService.findturnById(1).get().getGame().getId());
    }

    @Test
    void testGetState() {
        // TODO Needs improvement
        assertEquals(TurnStateType.MARKET_STATE, turnService.getState(turnService.findturnById(1).get()).getNextState());
    }

    @Test
    void testSetNextState() {
        turnService.setNextState(turnService.findturnById(1).get());
        assertEquals(TurnStateType.MARKET_STATE, turnService.findturnById(1).get().getStateType());
    }

    @Test
    void testCreateNextTurn() {
        turnService.createNextTurn(gameService.findGameById(1));
        assertEquals(gameService.findGameById(1).getPlayers().get(1), turnService.findturnById(2).get().getPlayer());
    }
    
}
