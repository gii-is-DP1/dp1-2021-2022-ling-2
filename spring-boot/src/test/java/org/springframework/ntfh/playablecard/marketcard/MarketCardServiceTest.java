package org.springframework.ntfh.playablecard.marketcard;

import static org.assertj.core.api.Assertions.assertThat;

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
class MarketCardServiceTest {

    @Autowired
    private MarketCardService marketCardService;

    private Integer INITIAL_MARKETCARD_COUNT = 14;


    @Test
    void testCountWithInitialData() {
        Integer count = marketCardService.marketCardCount();

        assertThat(count).isEqualTo(INITIAL_MARKETCARD_COUNT);
    }

    @Test
    void testfindAll() {
        Integer count = Lists.newArrayList(marketCardService.findAll()).size();

        assertThat(count).isEqualTo(INITIAL_MARKETCARD_COUNT);
    }

    @Test
    void testfindById() {
        MarketCard tester = this.marketCardService.findMarketCardById(6).get();
        Integer PRICE_OF_PIEDRA_DE_AMOLAR = 4;

        assertThat(tester.getMarketCardTypeEnum()).isEqualTo(MarketCardTypeEnum.PIEDRA_DE_AMOLAR);
        assertThat(tester.getPrice()).isEqualTo(PRICE_OF_PIEDRA_DE_AMOLAR);
    }

}
