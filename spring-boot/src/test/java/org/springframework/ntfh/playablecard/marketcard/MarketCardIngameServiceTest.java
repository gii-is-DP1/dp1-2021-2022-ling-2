package org.springframework.ntfh.playablecard.marketcard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
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
@DataJpaTest(
        includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
public class MarketCardIngameServiceTest {

    @Autowired
    private MarketCardService marketCardService;

    @Autowired
    private MarketCardIngameService marketCardIngameService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    protected MarketCardIngame cardTester;

    protected Game gameTester;

    protected Integer FULL_MARKET = 5;


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

        Player playerTester = gameTester.getPlayers().get(0);

        Character warriorCharacter = characterService.findById(5);

        playerTester.setCharacter(warriorCharacter);

        gameService.startGame(gameTester.getId());
        /******************************************************************/

        cardTester = marketCardIngameService
                .createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameTester);
    }

    @AfterEach
    void teardown() {
        try {
            marketCardIngameService.delete(cardTester);
        } catch (Exception exception) {
        }

    }

    @Test
    void testFindById() {
        MarketCardIngame testerCard = marketCardIngameService.findById(cardTester.getId());

        assertThat(testerCard.getMarketCard().getMarketCardTypeEnum())
                .isEqualTo(MarketCardTypeEnum.POCION_CURATIVA);
    }

    @Test
    void testSave() {
        cardTester = marketCardIngameService
                .createFromMarketCard(marketCardService.findMarketCardById(1).get(), gameTester);
        marketCardIngameService.save(cardTester);

        assertThat(marketCardIngameService.findById(cardTester.getId()).getMarketCard()
                .getMarketCardTypeEnum()).isEqualTo(MarketCardTypeEnum.DAGA_ELFICA);
    }

    @Test
    void testDelete() {
        marketCardIngameService.delete(cardTester);

        assertThrows(Exception.class, () -> {
            marketCardIngameService.findById(cardTester.getId());
        });
    }

    @Test
    void testRefillMarketWithCards() {
        gameTester.getLeader().setGold(10);
        String playerToken = TokenUtils.generateJWTToken(gameTester.getLeader().getUser());
        marketCardIngameService.initializeFromGame(gameTester);
        gameService.setNextTurnState(gameTester.getCurrentTurn());

        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET);

        List<MarketCardIngame> market = gameTester.getMarketCardsForSale();
        market.get(0).setMarketCard(cardTester.getMarketCard());
        marketCardIngameService.buyMarketCard(market.get(0).getId(), playerToken);
        Integer FULL_MARKET_LESS_ONE = 4;

        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET_LESS_ONE);

        marketCardIngameService.refillMarketWithCards(gameTester);

        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET);
    }

    @Test
    void testInitializeFromGame() {
        marketCardIngameService.initializeFromGame(gameTester);

        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET);
    }

    @Test
    void testCreateFromMarketCard() {
        // TestMethod made in the init()
        assertThat(cardTester.getMarketCard().getMarketCardTypeEnum())
                .isEqualTo(MarketCardTypeEnum.POCION_CURATIVA);
    }

}
