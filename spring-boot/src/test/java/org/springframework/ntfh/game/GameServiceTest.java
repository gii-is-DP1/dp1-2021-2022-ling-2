package org.springframework.ntfh.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private MarketCardService marketCardService;

    @Autowired
    private MarketCardIngameService marketCardIngameService;

    @Autowired
    private TurnService turnService;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private EnemyService enemyService;

    @Autowired
    private EnemyIngameService enemyIngameService;

    @Autowired
    private AbilityCardService abilityCardService;

    private Game gameTester;

    private Player playerTester;

    private Integer INITIAL_GAMES_COUNT = 0;

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

        gameTester = gameService.joinGame(gameTester, user1);
        gameTester = gameService.joinGame(gameTester, user2);

        playerTester = gameTester.getPlayers().get(0);

        Character warriorCharacter = characterService.findById(5);

        playerTester.setCharacter(warriorCharacter);

        gameService.startGame(gameTester.getId());
    }

    @AfterEach
    void teardown() {
        gameService.delete(gameTester);
    }

    @Test
    void testCountWithInitialData() {
        Integer count = gameService.gameCount();

        assertThat(count).isEqualTo(INITIAL_GAMES_COUNT + 1);
    }

    @Test
    void testFindById() {
        Game tester = this.gameService.findGameById(1);

        assertThat(tester.getHasScenes()).isFalse();
        assertThat(tester.getLeader().getId()).isEqualTo(1);
    }

    @Test
    void testFindPlayersByGameId() {
        List<Player> testerList = gameService.findPlayersByGameId(1);

        assertThat(testerList.size()).isEqualTo(2);
    }

    @Test
    void testGetCurrentTurnByGameId() {
        Integer tester = gameService.getCurrentTurnByGameId(gameTester.getId()).getId();

        assertThat(tester).isEqualTo(1);
    }

    @Test
    void testSaveGame_success() {
        // Test made in the init
        assertThat(gameTester.getId()).isEqualTo(gameService.findGameById(gameTester.getId()).getId());
    }

    @Test
    void testDeleteGame() {
        gameTester.setStateType(GameStateType.LOBBY);
        gameService.delete(gameTester);
        Integer gameId = gameTester.getId();

        assertThrows(DataAccessException.class, () -> gameService.findGameById(gameId));
    }

    @Test
    void testPlayCard() {
        AbilityCard pasoAtras = abilityCardService.findById(27);
        AbilityCardIngame abilityCardIngame = abilityCardIngameService.createFromAbilityCard(pasoAtras, playerTester);
        String token = TokenUtils.generateJWTToken(playerTester.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngame);
        playerTester.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngame.getId(), null, token);

        assertThat(playerTester.getHand().size()).isEqualTo(2);
    }

    @Test
    void testNextTurnState() {
        String player_state = gameTester.getCurrentTurn().getStateType().toString();

        assertThat(player_state).isEqualTo("PLAYER_STATE");

        gameService.setNextTurnState(gameTester.getCurrentTurn());

        String market_state = gameTester.getCurrentTurn().getStateType().toString();

        assertThat(market_state).isEqualTo("MARKET_STATE");

    }

    // H1 + E1
    @Test
    void testfindAll() {
        Integer count = Lists.newArrayList(gameService.findAll()).size();

        assertThat(count).isEqualTo(INITIAL_GAMES_COUNT + 1);
    }

    // H7 + E1
    @Test
    void testCreateFromLobby() {
        assertThat(gameTester.getId()).isEqualTo(gameService.findGameById(gameTester.getId()).getId());
    }

    // H21 + E1
    @Test
    void testRegularBountyCollection() {
        // Slinger de 2 de vida
        EnemyIngame enemyIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(12).get(), gameTester);
        new DealDamageCommand(2, playerTester, enemyIngame).execute();

        assertThat(playerTester.getGold()).isEqualTo(1);
        assertThat(playerTester.getGlory()).isEqualTo(1);
    }

    // H21 + E2
    @Test
    void testBountyBehaviourWithTrampaCard() {
        gameTester.getLeader().setCharacter(characterService.findById(3));
        AbilityCard trampa = abilityCardService.findById(60);
        AbilityCardIngame trampaIngame = abilityCardIngameService.createFromAbilityCard(trampa, playerTester);
        String token = TokenUtils.generateJWTToken(playerTester.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(trampaIngame);
        playerTester.setHand(hand);

        EnemyIngame enemyIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(12).get(), gameTester);
        List<EnemyIngame> enemiesFighting = new ArrayList<>();
        enemiesFighting.add(enemyIngame);
        gameTester.setEnemiesFighting(enemiesFighting);

        assertThat(gameTester.getEnemiesFighting().contains(enemyIngame)).isTrue();

        abilityCardIngameService.playCard(trampaIngame.getId(), enemyIngame.getId(), token);

        turnService.createNextTurn(gameTester);

        assertThat(gameTester.getEnemiesFighting().contains(enemyIngame)).isFalse();
    }

    // H22 + E1
    @Test
    void testBuyMarketCard_Success() {
        MarketCardIngame marketCardIngame =
                marketCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameTester);
        Integer marketCardIngameId = marketCardIngame.getId();
        playerTester.setGold(10);
        String playerToken = TokenUtils.generateJWTToken(playerTester.getUser());
        gameService.setNextTurnState(gameService.getCurrentTurnByGameId(gameTester.getId()));
        marketCardIngameService.buyMarketCard(marketCardIngameId, playerToken);

        assertThat(playerTester.getGold()).isEqualTo(2);
    }

    // H22 - E1
    @Test
    void testBuyMarketCard_Failure() {
        MarketCardIngame marketCardIngame =
                marketCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameTester);
        Integer marketCardIngameId = marketCardIngame.getId();
        playerTester.setGold(4);
        String playerToken = TokenUtils.generateJWTToken(playerTester.getUser());
        gameService.setNextTurnState(gameTester.getCurrentTurn());

        assertThrows(IllegalArgumentException.class, () -> {
            marketCardIngameService.buyMarketCard(marketCardIngameId, playerToken);
        });
    }

    // H23 + E1
    @Test
    void testKillCount() {
        EnemyIngame enemyIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(12).get(), gameTester);
        new DealDamageCommand(2, playerTester, enemyIngame).execute();

        assertThat(playerTester.getKills()).isEqualTo(1);
    }

}
