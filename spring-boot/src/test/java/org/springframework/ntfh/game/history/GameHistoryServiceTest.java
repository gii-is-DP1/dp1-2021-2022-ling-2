package org.springframework.ntfh.game.history;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.game.history.GameHistoryService;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class GameHistoryServiceTest {

    @Autowired
    private GameHistoryService gameHistoryService;

    @Test
    public void testCountWithInitialData() {
        Integer count = gameHistoryService.count();
        assertEquals(1, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(gameHistoryService.findAll()).size();
        assertEquals(1, count);
    }
}
