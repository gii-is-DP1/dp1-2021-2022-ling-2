package org.springframework.ntfh.turn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.scene.SceneService;
import org.springframework.ntfh.entity.scene.SceneTypeEnum;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

// TODO Improve the teardown to increase the speed of the test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest(
        includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class TurnServiceTest {

    @Autowired
    private TurnService turnService;

    @Autowired
    private GameService gameService;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    protected Turn turnTester;

    @BeforeEach
    void setup() {
        /**************** Copypasted section ******************************/
        // TODO copypaste from CommandIngameTest, maybe extract to a method?
        Game gameTester = new Game();
        gameTester.setName("test game");
        gameTester.setHasScenes(false);
        gameTester.setSpectatorsAllowed(false);
        gameTester.setMaxPlayers(2);
        gameTester.setStateType(GameStateType.LOBBY);
        gameTester = gameService.save(gameTester);

        User user1 = userService.findUser("user1");
        User user2 = userService.findUser("user2");

        gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
        gameTester = gameService.joinGame(gameTester, user2);

        Player playerTester = gameTester.getPlayers().get(0);

        Character warriorCharacter = characterService.findById(5);

        playerTester.setCharacter(warriorCharacter);

        gameService.startGame(gameTester.getId());
        /******************************************************************/

        turnTester = turnService.findturnById(1).get();
    }

    @AfterEach
    void teardown() {
        try {
            turnService.delete(turnTester.getId());
        } catch (Exception exception) {
        }
    }

    @Test
    void testCount() {
        Integer counter = turnService.turnCount();

        assertThat(counter).isEqualTo(1);
    }

    @Test
    void testFindAll() {
        Integer counter = Lists.newArrayList(turnService.findAll()).size();

        assertThat(counter).isEqualTo(1);
    }

    @Test
    void testFindTurnById() {
        Turn tester = turnService.findturnById(1).get();

        assertThat(tester.getGame().getId()).isEqualTo(1);

    }

    @Test
    void testSaveTurn() {
        Turn testerSaver = turnTester;
        testerSaver.setCurrentScene(sceneService.findSceneById(1).get());
        turnService.save(testerSaver);

        assertThat(testerSaver.getCurrentScene().getSceneTypeEnum())
                .isEqualTo(SceneTypeEnum.MERCADO_DE_LOTHARION);
    }

    @Test
    void testDelete() {
        turnService.delete(turnTester.getId());

        assertThrows(Exception.class, () -> {
            turnService.findturnById(1).get();
        });
    }

    @Test
    void testInitializeFromGame() {
        // Test method made in the init()
        Integer ID_TURN = 1;
        assertThat(turnTester.getGame().getId()).isEqualTo(ID_TURN);
    }

    @Test
    void testGetState() {
        // TODO Needs improvement
        assertThat(turnService.getState(turnTester).getNextState())
                .isEqualTo(TurnStateType.MARKET_STATE);
    }

    @Test
    void testSetNextState() {
        turnService.setNextState(turnTester);

        assertThat(turnTester.getStateType()).isEqualTo(TurnStateType.MARKET_STATE);
    }

    @Test
    void testCreateNextTurn() {
        turnService.createNextTurn(gameService.findGameById(1));

        assertThat(turnService.findturnById(2).get().getPlayer())
                .isEqualTo(gameService.findGameById(1).getPlayers().get(1));
    }

}
