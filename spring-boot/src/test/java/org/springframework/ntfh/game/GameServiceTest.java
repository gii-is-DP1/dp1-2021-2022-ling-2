package org.springframework.ntfh.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameRepository;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

// TODO Improve the teardown to increase the speed of the test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LobbyService lobbyService;

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

    protected Lobby lobbyTester;

    private Player playerTester;

    private Integer INITIAL_GAMES_COUNT = 3;

    @BeforeEach
    public void init() {
        User user1 = userService.findUser("user1");
        User user2 = userService.findUser("user2");
        Set<User> users = Sets.newSet(user1, user2);

        lobbyTester = new Lobby();
        lobbyTester.setName("Init lobby");
        lobbyTester.setHasScenes(true);
        lobbyTester.setSpectatorsAllowed(false);
        lobbyTester.setMaxPlayers(4);
        lobbyTester.setUsers(users);
        lobbyTester.setHost(user1);
        lobbyTester.setLeader(user1);
        lobbyService.save(lobbyTester);

        user1.setCharacter(characterService.findCharacterById(2).get());
        user2.setCharacter(characterService.findCharacterById(4).get());

        gameTester = gameService.createFromLobby(lobbyTester);
        user1.setLobby(lobbyTester);
        playerTester = gameTester.getPlayers().get(0);
    }

    @AfterEach
    public void teardown() {
        gameService.delete(gameTester);
    }

    @Test
    public void testCountWithInitialData() {
        Integer count = gameService.gameCount();
        assertEquals(INITIAL_GAMES_COUNT + 1, count);
    }

    @Test
    public void testFindById() {
        Game tester = this.gameService.findGameById(1);
        assertEquals(true, tester.getHasScenes());
        assertEquals(1, tester.getLeader().getId());
    }

    @Test
    void testFindPlayersByGameId() {
        List<Player> testerList = gameService.findPlayersByGameId(1);
        assertEquals(2, testerList.size());
    }

    //////////////
    @Test
    void testGetCurrentTurnByGameId() {
        Integer tester = gameService.getCurrentTurnByGameId(gameTester.getId()).getId();
        assertEquals(1, tester);
    }

    ///////////////
    @Test
    public void testSaveGame_success() {
        // Test made in the init
        assertEquals(gameRepository.findById(gameTester.getId()).get().getId(), gameTester.getId());
    }

    @Test
    public void testDeleteGame() {
        Game tester = gameService.createFromLobby(lobbyTester);
        gameService.delete(tester);
        assertThrows(DataAccessException.class, () -> {
            gameService.findGameById(tester.getId());
        });
    }

    @Test
    void testPlayCard() {
        playerTester.setCharacterType(characterService.findCharacterById(5).get());
        AbilityCard pasoAtras = abilityCardService.findById(27);
        AbilityCardIngame abilityCardIngame = abilityCardIngameService.createFromAbilityCard(pasoAtras, playerTester);
        String token = TokenUtils.generateJWTToken(playerTester.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngame);
        playerTester.setHand(hand);
        gameService.playCard(abilityCardIngame.getId(), null, token);
        assertEquals(2, playerTester.getHand().size());
    }

    @Test
    void testNextTurnState() {
        String player_state = gameTester.getCurrentTurn().getStateType().toString();
        assertEquals("PLAYER_STATE", player_state);
        gameService.setNextTurnState(gameTester.getCurrentTurn());
        String market_state = gameTester.getCurrentTurn().getStateType().toString();
        assertEquals("MARKET_STATE", market_state);

    }

    // H1 + E1
    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(gameService.findAll()).size();
        assertEquals(INITIAL_GAMES_COUNT + 1, count);
    }

    // H7 + E1
    @Test
    public void testCreateFromLobby() {
        Game tester = gameService.createFromLobby(lobbyTester);
        assertEquals(gameService.findGameById(tester.getId()).getId(), tester.getId());
        gameService.delete(tester);
    }

    // H7 - E1
    @Test
    public void testCreateFromLobbyNotEnoughPlayers() {
        User user2 = userService.findUser("user2");
        lobbyTester.removeUser(user2);
        assertThrows(IllegalArgumentException.class, () -> gameService.createFromLobby(lobbyTester));
    }

    // H25 + E1
    @Test
    void testRegularBountyCollection() {
        // Slinger de 2 de vida
        EnemyIngame enemyIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(12).get(), gameTester);
        turnService.initializeFromGame(gameTester);
        new DealDamageCommand(2, playerTester, enemyIngame).execute();

        assertEquals(1, playerTester.getGold());
        assertEquals(1, playerTester.getGlory());
    }

    // H25 + E2
    @Test
    void testBountyBehaviourWithTrampaCard() {
        turnService.initializeFromGame(gameTester);
        gameTester.getLeader().setCharacterType(characterService.findCharacterById(3).get());
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

        assertEquals(true, gameTester.getEnemiesFighting().contains(enemyIngame));

        gameService.playCard(trampaIngame.getId(), enemyIngame.getId(), token);

        // gameService.setNextTurnState(gameService.getCurrentTurnByGameId(gameTester.getId()));
        // gameService.setNextTurnState(gameService.getCurrentTurnByGameId(gameTester.getId()));
        // gameService.setNextTurnState(gameTester.getCurrentTurn());
        // gameService.setNextTurnState(gameTester.getCurrentTurn());
        // turnService.setNextState(gameService.getCurrentTurnByGameId(1));
        // turnService.setNextState(gameService.getCurrentTurnByGameId(1));
        turnService.createNextTurn(gameTester);

        assertEquals(false, gameTester.getEnemiesFighting().get(0));
    }

    // H26 + E1
    @Test
    void testBuyMarketCard_Success() {
        MarketCardIngame marketCardIngame = marketCardIngameService
                .createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameTester);
        Integer marketCardIngameId = marketCardIngame.getId();
        playerTester.setGold(10);
        String playerToken = TokenUtils.generateJWTToken(playerTester.getUser());
        turnService.initializeFromGame(gameTester);
        gameService.setNextTurnState(gameService.getCurrentTurnByGameId(gameTester.getId()));
        gameService.buyMarketCard(marketCardIngameId, playerToken);

        assertEquals(2, playerTester.getGold());
    }

    // H26 - E1
    @Test
    void testBuyMarketCard_Failure() {
        MarketCardIngame marketCardIngame = marketCardIngameService
                .createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameTester);
        Integer marketCardIngameId = marketCardIngame.getId();
        playerTester.setGold(4);
        String playerToken = TokenUtils.generateJWTToken(playerTester.getUser());
        turnService.initializeFromGame(gameTester);
        gameService.setNextTurnState(gameTester.getCurrentTurn());

        assertThrows(IllegalArgumentException.class, () -> {
            gameService.buyMarketCard(marketCardIngameId, playerToken);
        });
    }

    // H27 + E1
    @Test
    void testKillCount() {
        EnemyIngame enemyIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(12).get(), gameTester);
        turnService.initializeFromGame(gameTester);
        new DealDamageCommand(2, playerTester, enemyIngame).execute();

        assertEquals(1, playerTester.getKills());
    }

}
