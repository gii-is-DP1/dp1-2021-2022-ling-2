package org.springframework.ntfh.commandtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.GiveGuardCommand;
import org.springframework.ntfh.command.GiveWoundCommand;
import org.springframework.ntfh.command.GoldOnKillCommand;
import org.springframework.ntfh.command.HandToAbilityPileCommand;
import org.springframework.ntfh.command.HealCommand;
import org.springframework.ntfh.command.ReceiveDamageCommand;
import org.springframework.ntfh.command.RecoverCardCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.command.StealCoinCommand;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
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

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class commandIngameTest {
    
    @Autowired
    private GameService gameService;

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

    protected Game gameTester;

    protected Lobby lobbyTester;

    protected Player ranger;

    protected Player rogue;

    protected EnemyIngame enemyIngame;

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

        ranger = gameTester.getPlayers().get(0);
        rogue = gameTester.getPlayers().get(1);

        enemyIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(12).get(), gameTester);
    }

    @AfterEach
    public void teardown() {
        gameService.delete(gameTester);
    }


    @Test
    void testDealDamageCommand() {
        turnService.initializeFromGame(gameTester);
        Integer initialEndurance = enemyIngame.getCurrentEndurance();

        assertThat(enemyIngame.getCurrentEndurance()).isEqualTo(initialEndurance);

        new DealDamageCommand(1, ranger, enemyIngame).execute();

        assertThat(enemyIngame.getCurrentEndurance()).isEqualTo(initialEndurance-1);
    }

    @Test
    void testGiveGuard() {
        assertThat(ranger.getGuard()).isZero();
        
        new GiveGuardCommand(1, ranger).execute();

        assertThat(ranger.getGuard()).isEqualTo(1);
    }

    @Test
    void testGiveWound() {
        assertThat(rogue.getWounds()).isZero();
        
        new GiveWoundCommand(rogue).execute();

        assertThat(rogue.getWounds()).isEqualTo(1);
    }

    @Test
    void testGoldOnKill() {
        turnService.initializeFromGame(gameTester);
        enemyIngame.setCurrentEndurance(0);
        new GoldOnKillCommand(1, enemyIngame, ranger).execute();

        assertThat(ranger.getGold()).isEqualTo(1);
    }

    @Test
    void testHandToAbilityPile() {
        turnService.initializeFromGame(gameTester);
        AbilityCard companeroLobo = abilityCardService.findById(1);
        AbilityCardIngame abilityCardIngame = abilityCardIngameService.createFromAbilityCard(companeroLobo, ranger);
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngame);
        ranger.setHand(hand);

        assertThat(ranger.getAbilityPile().size()).isEqualTo(15);
        assertThat(ranger.getHand().size()).isEqualTo(1);
        
        new HandToAbilityPileCommand(ranger, companeroLobo.getAbilityCardTypeEnum()).execute();

        assertThat(ranger.getAbilityPile().size()).isEqualTo(16);
        assertThat(ranger.getHand().size()).isZero();
    }

    @Test
    void testHeal() {
        rogue.setWounds(1);

        assertThat(rogue.getWounds()).isEqualTo(1);

        new HealCommand(rogue).execute();

        assertThat(rogue.getWounds()).isZero();
    }

    @Test
    void testReceiveDamage() {
        assertThat(rogue.getDiscardPile().size()).isZero();

        new ReceiveDamageCommand(enemyIngame.getCurrentEndurance(), enemyIngame, rogue).execute();

        assertThat(rogue.getDiscardPile().size()).isEqualTo(2);
    }

    @Test
    void testRecoverCardCommand() {
        turnService.initializeFromGame(gameTester);
        AbilityCard disparoRapido = abilityCardService.findById(4);
        AbilityCardIngame abilityCardIngame = abilityCardIngameService.createFromAbilityCard(disparoRapido, ranger);
        String token = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngame);
        ranger.setHand(hand);
        gameService.playCard(abilityCardIngame.getId(), gameTester.getEnemiesFighting().get(0).getId(), token);

        assertThat(ranger.getDiscardPile().size()).isEqualTo(1);

        new RecoverCardCommand(ranger, AbilityCardTypeEnum.DISPARO_RAPIDO).execute();

        assertThat(ranger.getDiscardPile().size()).isZero();
    }

    @Test
    void testRecoverCommand() {
        turnService.initializeFromGame(gameTester);

        new DiscardCommand(1, ranger).execute();
        new RecoverCommand(ranger).execute();

        assertThat(ranger.getDiscardPile().size()).isZero();
    }

    @Test
    void testRestrainCommand() {
        assertThat(enemyIngame.getRestrained()).isFalse();

        new RestrainCommand(enemyIngame).execute();

        assertThat(enemyIngame.getRestrained()).isTrue();
    }

    @Test
    void testStealCoinCommand() {
        ranger.setGold(10);

        assertThat(rogue.getGold()).isZero();
        assertThat(ranger.getGold()).isEqualTo(10);

        new StealCoinCommand(rogue, ranger).execute();

        assertThat(rogue.getGold()).isEqualTo(1);
        assertThat(ranger.getGold()).isEqualTo(9);
    }
    
}
