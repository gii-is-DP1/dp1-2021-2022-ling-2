package org.springframework.ntfh.cardtest;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.GiveWoundCommand;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
public class WizardCardTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private EnemyService enemyService;

    @Autowired
    private EnemyIngameService enemyIngameService;

    @Autowired
    private AbilityCardService abilityCardService;

    protected Game gameTester;

    protected Player wizard;

    protected Player rogue;

    protected EnemyIngame slingerIngame;

    protected EnemyIngame berserkerIngame;

    @BeforeEach
    public void init() {

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

        wizard = gameTester.getPlayers().get(0);
        rogue = gameTester.getPlayers().get(1);

        Character wizardCharacter = characterService.findById(8);
        Character rogueCharacter = characterService.findById(4);

        wizard.setCharacter(wizardCharacter);
        rogue.setCharacter(rogueCharacter);

        gameService.startGame(gameTester.getId());
        Enemy SLINGER = enemyService.findEnemyById(12).get();
        slingerIngame = enemyIngameService.createFromEnemy(SLINGER, gameTester);
        Enemy BERSERKER = enemyService.findEnemyById(14).get();
        berserkerIngame = enemyIngameService.createFromEnemy(BERSERKER, gameTester);
    }

    @AfterEach
    public void teardown() {
        gameService.delete(gameTester);
    }

    @Test
    void testAuraProtectora() {

        List<EnemyIngame> enemiesFighting = List.of(slingerIngame, berserkerIngame);
        gameTester.setEnemiesFighting(enemiesFighting);
        AbilityCard auraProtectora = abilityCardService.findById(31);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(auraProtectora, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), null, tokenWizard);

        assertThat(wizard.getDiscardPile().size()).isEqualTo(3); // one card discarded per enemy and the card played
        assertThat(slingerIngame.getRestrained()).isTrue();
        assertThat(berserkerIngame.getRestrained()).isTrue();
    }

    @Test
    void testBolaDeFuego() {

        List<EnemyIngame> enemiesFighting = new ArrayList<>();
        enemiesFighting.add(slingerIngame);
        enemiesFighting.add(berserkerIngame);
        gameTester.setEnemiesFighting(enemiesFighting);
        AbilityCard bolaDeFuego = abilityCardService.findById(32);
        AbilityCardIngame abilityCardIngameWizard = abilityCardIngameService.createFromAbilityCard(bolaDeFuego, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), null, tokenWizard);

        assertThat(slingerIngame.getCurrentEndurance()).isZero();
        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(rogue.getDiscardPile().size()).isEqualTo(1);
    }

    @Test
    void testDisparoGelido() {

        AbilityCard disparoGelido = abilityCardService.findById(33);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(disparoGelido, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), berserkerIngame.getId(), tokenWizard);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(5);
        assertThat(berserkerIngame.getRestrained()).isTrue();
        assertThat(wizard.getHand().size()).isEqualTo(1); // we draw 1 card

    }

    @Test
    void testFlechaCorrosiva() {

        AbilityCard flechaCorrosiva = abilityCardService.findById(35);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(flechaCorrosiva, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), berserkerIngame.getId(), tokenWizard);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(5);
        assertThat(berserkerIngame.getPlayedCardsOnMeInTurn()).contains(AbilityCardTypeEnum.FLECHA_CORROSIVA);
        assertThat(wizard.getDiscardPile().size()).isEqualTo(2); // we discard 1 and the played card is also discarded

        // any other card played after this one on the same enemy will cause 1 additional damage

        AbilityCard disparoGelido = abilityCardService.findById(33);
        AbilityCardIngame disparoGelidoIngame = abilityCardIngameService.createFromAbilityCard(disparoGelido, wizard);

        hand.add(disparoGelidoIngame);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(disparoGelidoIngame.getId(), berserkerIngame.getId(), tokenWizard);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(3);
    }

    @Test
    void testGolpeDeBaston() {

        // base damage of the card

        List<EnemyIngame> listEnemiesFighting = List.of(berserkerIngame);
        gameTester.setEnemiesFighting(listEnemiesFighting);
        AbilityCard golpeDeBaston = abilityCardService.findById(36);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(golpeDeBaston, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), berserkerIngame.getId(), tokenWizard);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(5);
        assertThat(berserkerIngame.getPlayedCardsOnMeInTurn()).contains(AbilityCardTypeEnum.GOLPE_DE_BASTON);


        // this type of attack has already been used in this turn so damage is incremented

        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), berserkerIngame.getId(), tokenWizard);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(3);
        assertThat(berserkerIngame.getPlayedCardsOnMeInTurn()).contains(AbilityCardTypeEnum.GOLPE_DE_BASTON);
    }

    @Test
    void testOrbeCurativo() {

        new DiscardCommand(3, rogue).execute();
        new GiveWoundCommand(wizard).execute();
        new DiscardCommand(2, wizard).execute();

        assertThat(rogue.getDiscardPile().size()).isEqualTo(3);
        assertThat(wizard.getDiscardPile().size()).isEqualTo(2);
        assertThat(wizard.getWounds()).isEqualTo(1);

        AbilityCard orbeCurativo = abilityCardService.findById(40);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(orbeCurativo, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), null, tokenWizard);

        assertThat(rogue.getDiscardPile().size()).isEqualTo(1);
        assertThat(wizard.getWounds()).isZero();
        assertThat(wizard.getDiscardPile().size()).isZero(); // the card played has the exile property, thus it didnt go
                                                             // to the discard pile
    }

    @Test
    void testProyectilIgneo() {

        AbilityCard proyectilIgneo = abilityCardService.findById(41);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(proyectilIgneo, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), berserkerIngame.getId(), tokenWizard);

        assertThat(wizard.getGlory()).isEqualTo(1);
        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
    }

    @Test
    void testReconstitucion() {

        new DiscardCommand(2, wizard).execute();

        assertThat(wizard.getDiscardPile().size()).isEqualTo(2);

        AbilityCard reconstitucion = abilityCardService.findById(44);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(reconstitucion, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), null, tokenWizard);

        assertThat(wizard.getHand().size()).isEqualTo(1); // 1 card drawn
        assertThat(wizard.getDiscardPile().size()).isEqualTo(1); // the card played is discarded but the other 2 have
                                                                 // been recovered
    }

    @Test
    void testTorrenteDeLuz() {

        new DiscardCommand(3, wizard).execute();
        new DiscardCommand(2, rogue).execute();

        assertThat(wizard.getDiscardPile().size()).isEqualTo(3);
        assertThat(rogue.getDiscardPile().size()).isEqualTo(2);

        AbilityCard torrenteDeLuz = abilityCardService.findById(45);
        AbilityCardIngame abilityCardIngameWizard =
                abilityCardIngameService.createFromAbilityCard(torrenteDeLuz, wizard);
        String tokenWizard = TokenUtils.generateJWTToken(wizard.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameWizard);
        wizard.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameWizard.getId(), berserkerIngame.getId(), tokenWizard);

        assertThat(wizard.getDiscardPile().size()).isEqualTo(2);
        assertThat(rogue.getDiscardPile().size()).isZero();
        assertThat(wizard.getGlory()).isEqualTo(1);
        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
    }
}
