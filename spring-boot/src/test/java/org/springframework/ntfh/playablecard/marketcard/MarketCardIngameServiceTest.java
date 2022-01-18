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
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class MarketCardIngameServiceTest {

    @Autowired
    private MarketCardService marketCardService;

    @Autowired
    private MarketCardIngameService marketCardIngameService;

    @Autowired
    private GameService gameService;

    @Autowired
    private TurnService turnService;

    protected MarketCardIngame cardTester;

    protected Game gameTester;

    protected Integer FULL_MARKET = 5;


    @BeforeEach
    void init() {
        cardTester = marketCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameService.findGameById(1));
        gameTester = gameService.findGameById(1);
    }

    @AfterEach
    void teardown() {
        try {
            marketCardIngameService.delete(cardTester);
        } catch (Exception exception){}
        
    }

    @Test
    void testFindById() {
        MarketCardIngame testerCard = marketCardIngameService.findById(cardTester.getId());

        assertThat(testerCard.getMarketCard().getMarketCardTypeEnum()).isEqualTo(MarketCardTypeEnum.POCION_CURATIVA);
    }

    @Test
    void testSave() {
        cardTester = marketCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(1).get(), gameService.findGameById(1));
        marketCardIngameService.save(cardTester);

        assertThat(marketCardIngameService.findById(cardTester.getId()).getMarketCard().getMarketCardTypeEnum()).isEqualTo(MarketCardTypeEnum.DAGA_ELFICA);
    }

    @Test
    void testDelete() {
        marketCardIngameService.delete(cardTester);

        assertThrows(Exception.class, () -> {marketCardIngameService.findById(cardTester.getId());});
    } 
    
    @Test
    void testRefillMarketWithCards() {  
        gameService.findGameById(1).getLeader().setGold(10);
        String playerToken = TokenUtils.generateJWTToken(gameService.findGameById(1).getLeader().getUser());
        turnService.initializeFromGame(gameService.findGameById(1));
        marketCardIngameService.initializeFromGame(gameService.findGameById(1));
        gameService.setNextTurnState(gameService.getCurrentTurnByGameId(1));

        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET);

        List<MarketCardIngame> market = gameService.findGameById(1).getMarketCardsForSale();
        market.get(0).setMarketCard(cardTester.getMarketCard());
        gameService.buyMarketCard(market.get(0).getId(), playerToken);
        Integer FULL_MARKET_LESS_ONE = 4;

        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET_LESS_ONE);

        marketCardIngameService.refillMarketWithCards(gameService.findGameById(1));

        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET);
    }

    @Test
    void testInitializeFromGame() {
        marketCardIngameService.initializeFromGame(gameService.findGameById(1));
        assertThat(gameTester.getMarketCardsForSale().size()).isEqualTo(FULL_MARKET);
    }

    @Test
    void testCreateFromMarketCard() {
        // TestMethod made in the init()
        assertThat(cardTester.getMarketCard().getMarketCardTypeEnum()).isEqualTo(MarketCardTypeEnum.POCION_CURATIVA);
    }

}
