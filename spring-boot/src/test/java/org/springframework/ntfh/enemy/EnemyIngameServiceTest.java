package org.springframework.ntfh.enemy;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameRepository;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class EnemyIngameServiceTest {

    @Autowired
    private EnemyService enemyService;

    @Autowired
    private EnemyIngameRepository enemyIngameRepository;

    @Autowired
    private EnemyIngameService enemyIngameService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    private EnemyIngame enemyIngameTester;

    private Game gameTester;

    private Integer INITIAL_ENEMIESINGAME_COUNT = 20;


    @BeforeEach
    void init() {
        gameTester = new Game();
        gameTester.setName("test game");
        gameTester.setHasScenes(false);
        gameTester.setSpectatorsAllowed(false);
        gameTester.setMaxPlayers(2);
        gameTester.setStateType(GameStateType.LOBBY);
        gameTester = gameService.save(gameTester);

        User user1 = userService.findByUsername("user1");
        User user2 = userService.findByUsername("user2");

        gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
        gameTester = gameService.joinGame(gameTester, user2);

        Player playerTester = gameTester.getPlayers().get(0);

        Character warriorCharacter = characterService.findById(5);

        playerTester.setCharacter(warriorCharacter);

        gameService.startGame(gameTester.getId());
    }

    @Test
    void testEnemyIngameCount() {
        Integer counter = enemyIngameService.enemyIngameCount();

        assertThat(counter).isEqualTo(INITIAL_ENEMIESINGAME_COUNT);
    }

    @Test
    void testFindAll() {
        Integer counter = Lists.newArrayList(enemyIngameService.findAll()).size();

        assertThat(counter).isEqualTo(INITIAL_ENEMIESINGAME_COUNT);
    }

    @Test
    void testFindById() {
        Enemy enemyTester = enemyService.findEnemyById(1).get();
        enemyIngameTester = enemyIngameService.createFromEnemy(enemyTester, gameTester);
        Integer ENEMY_ENDURANCE = 4;

        assertThat(enemyIngameTester.getCurrentEndurance()).isEqualTo(ENEMY_ENDURANCE);
        assertThat(enemyIngameTester.isHorde()).isTrue();
        assertThat(enemyIngameTester.getGame()).isEqualTo(gameTester);
        assertThat(enemyIngameTester.getEnemy()).isEqualTo(enemyTester);
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

        assertThat(testSave.getId()).isEqualTo(21);
        enemyIngameRepository.delete(testSave);
    }

    @Test
    void testRefillTableWithEnemies() {
        Integer ENEMIES_AFTER_ONE_GET_KILLED = 2;
        Integer ENEMIES_REFILLED = 3;
        new DealDamageCommand(50, gameTester.getPlayers().get(0), gameTester.getEnemiesFighting().get(0)).execute();

        assertThat(gameTester.getEnemiesFighting().size()).isEqualTo(ENEMIES_AFTER_ONE_GET_KILLED);

        enemyIngameService.refillTableWithEnemies(gameTester);

        assertThat(gameTester.getEnemiesFighting().size()).isEqualTo(ENEMIES_REFILLED);
    }

    @Test
    void testInitializeFromGame() {
        // Method made in the BeforeEach
        assertThat(enemyIngameService.enemyIngameCount()).isEqualTo(INITIAL_ENEMIESINGAME_COUNT);
    }

}
