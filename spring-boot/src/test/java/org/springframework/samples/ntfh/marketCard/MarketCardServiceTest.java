package org.springframework.samples.ntfh.marketCard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.ntfh.marketcard.MarketCard;
import org.springframework.samples.ntfh.marketcard.MarketCardService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MarketCardServiceTest {
    
    @Autowired
    private MarketCardService marketCardService;

    @Test
    public void testCountWithInitialData() {
        Integer count = marketCardService.marketCardCount();
        assertEquals(count, 0);
    }

}
