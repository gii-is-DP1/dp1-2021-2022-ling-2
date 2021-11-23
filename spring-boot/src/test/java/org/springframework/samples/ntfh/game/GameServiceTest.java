package org.springframework.samples.ntfh.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GameServiceTest {
    @Autowired
    private GameService gameService;

    // TODO replace with meaningful tests. This is a placeholder
    @Test
    public void testCountWithInitialData() {
        Integer count = gameService.gameCount();
        assertEquals(count, 0);
    }

}
