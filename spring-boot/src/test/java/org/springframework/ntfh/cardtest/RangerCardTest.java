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
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnStateType;
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
class RangerCardTest {

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

        ranger = gameTester.getPlayers().get(0);
        rogue = gameTester.getPlayers().get(1);

        Character rangerCharacter = characterService.findById(2);
        Character rogueCharacter = characterService.findById(4);

        ranger.setCharacter(rangerCharacter);
        rogue.setCharacter(rogueCharacter);

        gameService.startGame(gameTester.getId());
        Enemy SLINGER = enemyService.findEnemyById(12).get();
        slingerIngame = enemyIngameService.createFromEnemy(SLINGER, gameTester);
        Enemy BERSERKER = enemyService.findEnemyById(15).get();
        berserkerIngame = enemyIngameService.createFromEnemy(BERSERKER, gameTester);
    }

    @AfterEach
    public void teardown() {
        gameService.delete(gameTester);
    }

    @Test
    void testCompaneroLobo() {

        AbilityCard companeroLobo = abilityCardService.findById(1);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(companeroLobo, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), berserkerIngame.getId(), tokenRanger);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(ranger.getGuard()).isEqualTo(2);
    }

    @Test
    void disparoCertero() {

        Integer gameId = gameTester.getId();
        Turn currentTurn = gameService.getCurrentTurnByGameId(gameId);
        TurnStateType initialTurnState = currentTurn.getStateType();

        AbilityCard disparoCertero = abilityCardService.findById(2);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(disparoCertero, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), berserkerIngame.getId(), tokenRanger);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(3);
        assertThat(currentTurn.getStateType()).isNotEqualTo(initialTurnState); // the game has changed from player phase
                                                                               // to market phase

    }

    @Test
    void disparoRapido() {

        // creation of different cards for this test

        AbilityCard disparoRapido = abilityCardService.findById(4);
        AbilityCard disparoRapido2 = abilityCardService.findById(5);
        AbilityCard companeroLobo = abilityCardService.findById(1);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(disparoRapido, ranger);
        AbilityCardIngame abilityCardIngameRanger2 =
                abilityCardIngameService.createFromAbilityCard(disparoRapido2, ranger);
        AbilityCardIngame companeroLoboIngame = abilityCardIngameService.createFromAbilityCard(companeroLobo, ranger);

        // the next card in the pile is a disparoRapido, and thus it should go to the hand

        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        List<AbilityCardIngame> abilityPile = new ArrayList<>();
        abilityPile.add(abilityCardIngameRanger2);
        abilityPile.add(companeroLoboIngame);
        ranger.setHand(hand);
        ranger.setAbilityPile(abilityPile);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), berserkerIngame.getId(), tokenRanger);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(5);
        assertThat(ranger.getHand()).hasSize(1);

        // the next card in the pile is not a quickshot and so it will be returned to the draw pile

        abilityCardIngameService.playCard(abilityCardIngameRanger2.getId(), berserkerIngame.getId(), tokenRanger);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(ranger.getHand()).isEmpty();
    }

    @Test
    void testEnLaDiana() {

        AbilityCard enLaDiana = abilityCardService.findById(10);
        AbilityCardIngame abilityCardIngameRanger = abilityCardIngameService.createFromAbilityCard(enLaDiana, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), berserkerIngame.getId(), tokenRanger);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(2);
        assertThat(ranger.getDiscardPile()).hasSize(2); // the card just played and the one discarded due to
                                                                 // its effect
    }

    @Test
    void testLluviaDeFlechas() {

        // initialize the enemies on table to allow for the lluvia de flechas to reach them
        // we use list instead of listof to avoid it giving an error if an enemy (the slinger) dies and is attempted to
        // be removed

        List<EnemyIngame> listEnemiesFighting = new ArrayList<>();
        listEnemiesFighting.add(berserkerIngame);
        listEnemiesFighting.add(slingerIngame);
        gameTester.setEnemiesFighting(listEnemiesFighting);


        AbilityCard enLaDiana = abilityCardService.findById(11);
        AbilityCardIngame abilityCardIngameRanger = abilityCardIngameService.createFromAbilityCard(enLaDiana, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), null, tokenRanger);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(slingerIngame.getCurrentEndurance()).isZero();
        assertThat(rogue.getDiscardPile()).hasSize(2);
    }

    @Test
    void testRecogerFlechas() {

        // test normal conditions

        AbilityCard disparoRapido = abilityCardService.findById(4);
        AbilityCard companeroLobo = abilityCardService.findById(1);
        AbilityCardIngame abilityCardIngameRanger2 =
                abilityCardIngameService.createFromAbilityCard(disparoRapido, ranger);
        AbilityCardIngame companeroLoboIngame = abilityCardIngameService.createFromAbilityCard(companeroLobo, ranger);

        List<AbilityCardIngame> discardPile = new ArrayList<>();
        discardPile.add(abilityCardIngameRanger2);
        ranger.setDiscardPile(discardPile);

        AbilityCard recogerFlechas = abilityCardService.findById(13);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(recogerFlechas, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), null, tokenRanger);

        assertThat(ranger.getDiscardPile()).hasSize(1); // just the recoger flechas that we just used
        assertThat(ranger.getGold()).isEqualTo(1);

        // the card will not find any disparo rapido in the discard pile

        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        discardPile.clear();
        discardPile.add(companeroLoboIngame);
        ranger.setDiscardPile(discardPile);

        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), null, tokenRanger);

        assertThat(ranger.getDiscardPile()).hasSize(2);
        assertThat(ranger.getGold()).isEqualTo(2);

    }

    @Test
    void testSupervivencia() {

        List<EnemyIngame> listEnemiesFighting = new ArrayList<>();
        listEnemiesFighting.add(berserkerIngame);
        listEnemiesFighting.add(slingerIngame);
        gameTester.setEnemiesFighting(listEnemiesFighting);


        AbilityCard enLaDiana = abilityCardService.findById(15);
        AbilityCardIngame abilityCardIngameRanger = abilityCardIngameService.createFromAbilityCard(enLaDiana, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(), berserkerIngame.getId(), tokenRanger);

        assertThat(gameTester.getEnemiesFighting()).doesNotContain(berserkerIngame);
        assertThat(gameTester.getEnemiesFighting()).hasSize(2);

    }
}
