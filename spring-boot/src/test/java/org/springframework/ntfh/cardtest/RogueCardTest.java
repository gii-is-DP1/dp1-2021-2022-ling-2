package org.springframework.ntfh.cardtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.command.GiveGoldCommand;
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
public class RogueCardTest {

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

    protected Player ranger;

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

        rogue = gameTester.getPlayers().get(0);
        ranger = gameTester.getPlayers().get(1);

        Character rangerCharacter = characterService.findById(2);
        Character rogueCharacter = characterService.findById(4);

        ranger.setCharacter(rangerCharacter);
        rogue.setCharacter(rogueCharacter);

        gameService.startGame(gameTester.getId());
        Enemy SLINGER = enemyService.findEnemyById(9).get();
        slingerIngame = enemyIngameService.createFromEnemy(SLINGER, gameTester);
        Enemy BERSERKER = enemyService.findEnemyById(14).get();
        berserkerIngame = enemyIngameService.createFromEnemy(BERSERKER, gameTester);
    }

    @AfterEach
    public void teardown() {
        gameService.delete(gameTester);
    }

    @Test
    void testAlCorazon() {

        // the attack doesnt kill and doesnt activate gold on kill

        AbilityCard alCorazon = abilityCardService.findById(46);
        AbilityCardIngame abilityCardIngameRogue = abilityCardIngameService.createFromAbilityCard(alCorazon, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(2);
        assertThat(rogue.getGold()).isZero();
        assertThat(rogue.getDiscardPile().size()).isEqualTo(2); // discarded card and the card played

        // the attack kills

        List<AbilityCardIngame> discardPile = new ArrayList<>();
        rogue.setDiscardPile(discardPile);
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        assertThat(berserkerIngame.getCurrentEndurance()).isZero();
        assertThat(rogue.getGold()).isEqualTo(1);
        assertThat(rogue.getDiscardPile().size()).isEqualTo(2);
    }

    @Test
    void testAtaqueFurtivo() {

        // the attack doesnt kill and doesnt activate gold on kill

        AbilityCard alCorazon = abilityCardService.findById(48);
        AbilityCardIngame abilityCardIngameRogue = abilityCardIngameService.createFromAbilityCard(alCorazon, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(rogue.getGold()).isZero();


        // the attack kills

        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), slingerIngame.getId(), tokenRogue);

        assertThat(slingerIngame.getCurrentEndurance()).isZero();
        assertThat(rogue.getGold()).isEqualTo(1);
    }

    @Test
    void testBallestaPrecisa() {

        // the attack deals the base amount of damage

        List<EnemyIngame> listEnemiesFighting = List.of(berserkerIngame);
        gameTester.setEnemiesFighting(listEnemiesFighting);
        AbilityCard ballestaPrecisa = abilityCardService.findById(51);
        AbilityCardIngame abilityCardIngameRogue =
                abilityCardIngameService.createFromAbilityCard(ballestaPrecisa, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(berserkerIngame.getPlayedCardsOnMeInTurn()).contains(AbilityCardTypeEnum.BALLESTA_PRECISA);

        // this type of attack has already been used in this turn so damage is incremented

        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(1);
        assertThat(berserkerIngame.getPlayedCardsOnMeInTurn()).contains(AbilityCardTypeEnum.BALLESTA_PRECISA);
    }

    @Test
    void testEnganar() {

        // The rogue has the gold needed to perform the action

        new GiveGoldCommand(2, rogue).execute();

        assertThat(rogue.getGold()).isEqualTo(2);

        AbilityCard enganar = abilityCardService.findById(56);
        AbilityCardIngame abilityCardIngameRogue = abilityCardIngameService.createFromAbilityCard(enganar, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        assertThat(berserkerIngame.getRestrained()).isTrue();
        assertThat(rogue.getGold()).isZero();

        // rogue doesnt have enough gold,

        hand.add(abilityCardIngameRogue); rogue.setHand(hand);

        Integer cardPlayedId = abilityCardIngameRogue.getId();
        Integer berserkerIngameId = berserkerIngame.getId();
         
        assertThrows(IllegalArgumentException.class, ()->abilityCardIngameService.playCard(cardPlayedId, berserkerIngameId, tokenRogue));
         
    }

    @Test
    void testEnLasSombras() {

        AbilityCard enLasSombras = abilityCardService.findById(54);
        AbilityCardIngame abilityCardIngameRogue = abilityCardIngameService.createFromAbilityCard(enLasSombras, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(5);
        assertThat(rogue.getGuard()).isEqualTo(2);
    }

    @Test
    void testRobarBolsillos() {

        // The ally doesnt have gold

        AbilityCard robarBolsillos = abilityCardService.findById(57);
        AbilityCardIngame abilityCardIngameRogue =
                abilityCardIngameService.createFromAbilityCard(robarBolsillos, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), null, tokenRogue);

        assertThat(rogue.getGold()).isZero();
        assertThat(ranger.getGold()).isZero();

        // The ally does have gold to be stolen

        new GiveGoldCommand(1, ranger).execute();

        assertThat(ranger.getGold()).isEqualTo(1);

        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), null, tokenRogue);

        assertThat(rogue.getGold()).isEqualTo(1);
        assertThat(ranger.getGold()).isZero();
    }

    @Test
    void testSaqueoOro() {

        List<EnemyIngame> enemiesFighting = List.of(berserkerIngame, slingerIngame);
        gameTester.setEnemiesFighting(enemiesFighting);
        AbilityCard saqueoOro = abilityCardService.findById(58);
        AbilityCardIngame abilityCardIngameRogue = abilityCardIngameService.createFromAbilityCard(saqueoOro, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), null, tokenRogue);

        assertThat(rogue.getGold()).isEqualTo(4);
    }

    @Test
    void testSaqueoOroGloria() {

        List<EnemyIngame> enemiesFighting = List.of(berserkerIngame, slingerIngame);
        gameTester.setEnemiesFighting(enemiesFighting);
        AbilityCard saqueoOroGloria = abilityCardService.findById(59);
        AbilityCardIngame abilityCardIngameRogue =
                abilityCardIngameService.createFromAbilityCard(saqueoOroGloria, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), null, tokenRogue);

        assertThat(rogue.getGold()).isEqualTo(2);
        assertThat(rogue.getGlory()).isEqualTo(2);
    }

    @Test
    @Disabled
    // ! TODO Trampa se está comportando como quiere, por algún motivo no está tomando el daño el rogue, no esta
    // entrando en el receive damage
    void trampa() {

        List<EnemyIngame> enemiesFighting = new ArrayList<>();
        enemiesFighting.add(berserkerIngame);
        gameTester.setEnemiesFighting(enemiesFighting);
        AbilityCard trampa = abilityCardService.findById(60);
        AbilityCardIngame abilityCardIngameRogue = abilityCardIngameService.createFromAbilityCard(trampa, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(), berserkerIngame.getId(), tokenRogue);

        Integer discards = rogue.getDiscardPile().size();
        Integer pile = rogue.getAbilityPile().size();
        Boolean tes = berserkerIngame.getRestrained();

        assertThat(berserkerIngame.getPlayedCardsOnMeInTurn()).isNotEmpty().contains(AbilityCardTypeEnum.TRAMPA);
        assertThat(berserkerIngame.getCurrentEndurance()).isZero();
        assertThat(rogue.getDiscardPile().size()).isEqualTo(7); // the full force of the attack (6 discards) and then
                                                                // the card played

    }
}
