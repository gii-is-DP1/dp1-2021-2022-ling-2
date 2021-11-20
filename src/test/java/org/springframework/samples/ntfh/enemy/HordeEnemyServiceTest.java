package org.springframework.samples.ntfh.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))


public class HordeEnemyServiceTest {
    
    @Autowired
    private HordeEnemyService hordeEnemyService;

    @Test
    public void testCountWithInitialData() {
        Integer count = hordeEnemyService.hordeEnemyCount();
        assertEquals(count, 27);
    }
    
}

