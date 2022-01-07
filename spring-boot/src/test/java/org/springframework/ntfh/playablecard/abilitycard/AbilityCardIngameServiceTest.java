package org.springframework.ntfh.playablecard.abilitycard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.command.HandToAbilityPileCommand;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardTypeEnum;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class AbilityCardIngameServiceTest {

    @Autowired
    private AbilityCardService abilityCardService;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private GameService gameService;

    @Autowired
    private TurnService turnService;

    @Autowired
    private MarketCardService marketCardService;

    private AbilityCardIngame cardTester;

    @BeforeEach
    void init() {
        cardTester = abilityCardIngameService.createFromAbilityCard(abilityCardService.findById(44), gameService.findGameById(1).getLeader());
    }

    @AfterEach
    void teardown() {
        try {
            abilityCardIngameService.delete(cardTester);
        } catch (Exception exception){}
        
    }

    @Test
    void testFindById() {
        AbilityCardIngame testerCard = abilityCardIngameService.findById(cardTester.getId());
        assertEquals(AbilityCardTypeEnum.RECONSTITUCION, testerCard.getAbilityCardTypeEnum());
    }

    @Test
    void testDelete() {
        abilityCardIngameService.delete(cardTester);
        assertThrows(Exception.class, () -> {abilityCardIngameService.findById(cardTester.getId());});
    }

    @Test
    void testInitializeFromGame() {
        // Because of populateWithInitialCards() is an auxiliary private method from initializeFromGame(), is tested in the test method of the primary method
        abilityCardIngameService.initializeFromGame(gameService.findGameById(1));
        assertEquals(4, gameService.findGameById(1).getLeader().getHand().size());
        assertEquals(11, gameService.findGameById(1).getLeader().getAbilityPile().size());
    }


    @Test
    void testCreateFromAbilityCard() {
        // TestMethod made in the init()
        assertEquals(AbilityCardTypeEnum.RECONSTITUCION, cardTester.getAbilityCardTypeEnum());
    }

    @Test
    void testCreateFromMarketCard() {
        cardTester = abilityCardIngameService.createFromMarketCard(marketCardService.findMarketCardById(3).get(), gameService.findGameById(1).getLeader());
        assertEquals(MarketCardTypeEnum.POCION_CURATIVA.toString(), cardTester.getAbilityCardTypeEnum().toString());
    }

    // H20 + E1
    @Test
    void testRefillHandWithCards() {
        turnService.initializeFromGame(gameService.findGameById(1));
        new HandToAbilityPileCommand(gameService.findGameById(1).getLeader(), gameService.findGameById(1).getLeader().getHand().get(0).getAbilityCardTypeEnum())
                .execute();
        assertEquals(3, gameService.findGameById(1).getLeader().getHand().size());
        abilityCardIngameService.refillHandWithCards(gameService.findGameById(1).getLeader());
        assertEquals(4, gameService.findGameById(1).getLeader().getHand().size());
    }

    
}
