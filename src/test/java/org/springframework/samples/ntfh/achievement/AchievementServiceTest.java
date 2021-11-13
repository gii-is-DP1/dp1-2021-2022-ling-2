package org.springframework.samples.ntfh.achievement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.Provider.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchievementServiceTest {
    
    @Autowired
    private AchievementService achievementService;

    @Test
    public void testCountWithInitialData(){
        Integer count = achievementService.achievementCount();
        assertEquals(count, 0);
    }

}
