package org.springframework.ntfh.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.IngameServiceTest;
import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameRepository;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class EnemyIngameServiceTest {
    
    @Autowired
    private EnemyService enemyService;
        
    @Autowired
    private EnemyIngameRepository enemyIngameRepository;

    @Autowired
    private EnemyIngameService enemyIngameService;

    @Autowired
    private GameService gameService;

    @Autowired
    private TurnService turnService;

    private EnemyIngame enemyIngameTester;

    private Game gameTester;

    private Integer INITIAL_ENEMIESINGAME_COUNT = 20;


    @BeforeEach
    void init() {
        gameTester = gameService.findGameById(1);
        enemyIngameService.initializeFromGame(gameTester);
    }

    @Test
    void testEnemyIngameCount() {
        Integer counter = enemyIngameService.enemyIngameCount();
        assertEquals(INITIAL_ENEMIESINGAME_COUNT, counter);
    }

    @Test
    void testFindAll() {
        Integer counter = Lists.newArrayList(enemyIngameService.findAll()).size();
        assertEquals(INITIAL_ENEMIESINGAME_COUNT, counter);
    }

    @Test
    void testFindById() {
        Enemy enemyTester = enemyService.findEnemyById(1).get();
        enemyIngameTester = enemyIngameService.createFromEnemy(enemyTester, gameTester);
        assertEquals(4, enemyIngameTester.getCurrentEndurance());
        assertEquals(true, enemyIngameTester.isHorde());
        assertEquals(gameTester, enemyIngameTester.getGame());
        assertEquals(enemyTester, enemyIngameTester.getEnemy());
    }
   
    @Test
    void testSaveEnemyIngame() {
        Enemy enemyTester = enemyService.findEnemyById(1).get();
        EnemyIngame testSave = new EnemyIngame();
        testSave.setEnemy(enemyTester);
        testSave.setGame(gameTester);
        testSave.setCurrentEndurance(4);
        testSave.setRestrained(false);
        enemyIngameService.save(testSave);

        assertEquals(21, testSave.getId());
        enemyIngameRepository.delete(testSave);
    }

    @Test
    void testRefillTableWithEnemies() {
        turnService.initializeFromGame(gameTester);
        new DealDamageCommand(50, gameTester.getPlayers().get(0), gameTester.getEnemiesFighting().get(0)).execute();
        assertEquals(2, gameTester.getEnemiesFighting().size());
        enemyIngameService.refillTableWithEnemies(gameTester);
        assertEquals(3, gameTester.getEnemiesFighting().size());
    }

    @Test
    void testInitializeFromGame() {
        // Method made in the BeforeEach
        assertEquals(INITIAL_ENEMIESINGAME_COUNT, enemyIngameService.enemyIngameCount());
    }

}
