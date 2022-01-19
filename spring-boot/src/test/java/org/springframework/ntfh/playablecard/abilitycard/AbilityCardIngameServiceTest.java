package org.springframework.ntfh.playablecard.abilitycard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.command.HandToAbilityPileCommand;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardTypeEnum;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(
        includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class AbilityCardIngameServiceTest {

    @Autowired
    private AbilityCardService abilityCardService;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private MarketCardService marketCardService;

    protected Game gameTester;

    protected AbilityCardIngame cardTester;

    protected Player playerTester;

    @BeforeEach
    void init() {
        /**************** Copypasted section ******************************/
        // TODO copypaste from CommandIngameTest, maybe extract to a method?
        gameTester = new Game();
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

        playerTester = gameTester.getPlayers().get(0);

        Character warriorCharacter = characterService.findById(5);

        playerTester.setCharacter(warriorCharacter);

        gameService.startGame(gameTester.getId());
        /******************************************************************/

        cardTester = abilityCardIngameService.createFromAbilityCard(abilityCardService.findById(44),
                playerTester);
    }

    @AfterEach
    void teardown() {
        try {
            abilityCardIngameService.delete(cardTester);
        } catch (Exception exception) {
        }

    }

    @Test
    void testFindById() {
        AbilityCardIngame testerCard = abilityCardIngameService.findById(cardTester.getId());

        assertThat(testerCard.getAbilityCardTypeEnum())
                .isEqualTo(AbilityCardTypeEnum.RECONSTITUCION);
    }

    @Test
    void testDelete() {
        abilityCardIngameService.delete(cardTester);

        assertThrows(Exception.class, () -> {
            abilityCardIngameService.findById(cardTester.getId());
        });
    }

    @Test
    void testInitializeFromGame() {
        // Because of populateWithInitialCards() is an auxiliary private method from
        // initializeFromGame(), is tested in
        // the test method of the primary method
        Integer HAND_SIZE = 4;
        Integer DECK_SIZE = 11;

        assertThat(playerTester.getHand().size()).isEqualTo(HAND_SIZE);
        assertThat(playerTester.getAbilityPile().size()).isEqualTo(DECK_SIZE);
    }


    @Test
    void testCreateFromAbilityCard() {
        // TestMethod made in the init()
        assertThat(cardTester.getAbilityCardTypeEnum())
                .isEqualTo(AbilityCardTypeEnum.RECONSTITUCION);
    }

    @Test
    void testCreateFromMarketCard() {
        cardTester = abilityCardIngameService
                .createFromMarketCard(marketCardService.findMarketCardById(3).get(), playerTester);

        assertThat(cardTester.getAbilityCardTypeEnum())
                .hasToString(MarketCardTypeEnum.POCION_CURATIVA.toString());
    }

    // H20 + E1
    @Test
    void testRefillHandWithCards() {
        new HandToAbilityPileCommand(playerTester,
                gameTester.getLeader().getHand().get(0).getAbilityCardTypeEnum()).execute();
        Integer HAND_AFTER_LOSING_ONE_CARD = 3;
        Integer HAND_AFTER_REFILL = 4;

        assertThat(gameTester.getLeader().getHand().size()).isEqualTo(HAND_AFTER_LOSING_ONE_CARD);

        abilityCardIngameService.refillHandWithCards(gameTester.getLeader());

        assertThat(playerTester.getHand().size()).isEqualTo(HAND_AFTER_REFILL);
    }

}
