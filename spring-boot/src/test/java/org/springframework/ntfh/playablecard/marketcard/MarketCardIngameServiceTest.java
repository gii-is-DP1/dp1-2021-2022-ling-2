package org.springframework.ntfh.playablecard.marketcard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
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

    private MarketCardIngame cardTester;

    @BeforeEach
    void init() {
        cardTester = marketCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameService.findGameById(1));
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
        assertEquals(MarketCardTypeEnum.POCION_CURATIVA, testerCard.getMarketCard().getMarketCardTypeEnum());
    }

    @Test
    void testSave() {
        cardTester = marketCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(1).get(), gameService.findGameById(1));
        marketCardIngameService.save(cardTester);
        assertEquals(MarketCardTypeEnum.DAGA_ELFICA, marketCardIngameService.findById(cardTester.getId()).getMarketCard().getMarketCardTypeEnum());
        cardTester = marketCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameService.findGameById(1));
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

        assertEquals(5, gameService.findGameById(1).getMarketCardsForSale().size());
        gameService.buyMarketCard(gameService.findGameById(1).getMarketCardsForSale().get(0).getId(), playerToken);
        assertEquals(4, gameService.findGameById(1).getMarketCardsForSale().size());
        marketCardIngameService.refillMarketWithCards(gameService.findGameById(1));
        assertEquals(5, gameService.findGameById(1).getMarketCardsForSale().size());
    }

    @Test
    void testInitializeFromGame() {
        marketCardIngameService.initializeFromGame(gameService.findGameById(1));
        assertEquals(5, gameService.findGameById(1).getMarketCardsForSale().size());
    }

    @Test
    void testCreateFromMarketCard() {
        // TestMethod made in the init()
        assertEquals(MarketCardTypeEnum.POCION_CURATIVA, cardTester.getMarketCard().getMarketCardTypeEnum());
    }

}
