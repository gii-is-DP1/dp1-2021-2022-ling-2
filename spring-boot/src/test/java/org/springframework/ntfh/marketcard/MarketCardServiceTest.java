package org.springframework.ntfh.marketcard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCard;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardTypeEnum;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class MarketCardServiceTest {

    @Autowired
    private MarketCardService marketCardService;

    @Test
    public void testCountWithInitialData() {
        Integer count = marketCardService.marketCardCount();
        assertEquals(14, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(marketCardService.findAll()).size();
        assertEquals(14, count);
    }

    @Test
    public void testfindById() {
        MarketCard tester = this.marketCardService.findMarketCardById(6).orElse(null);
        assertEquals(MarketCardTypeEnum.PIEDRA_DE_AMOLAR, tester.getMarketCardTypeEnum());
        assertEquals(4, tester.getPrice());
    }

}
