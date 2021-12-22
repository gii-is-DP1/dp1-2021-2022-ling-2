package org.springframework.ntfh.achievement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(BCryptPasswordEncoder.class)
public class AchievementServiceTest {

    @Autowired
    private AchievementService achievementService;

    @Test
    public void testCountWithInitialData() {
        Integer count = achievementService.achievementCount();
        assertEquals(3, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(achievementService.findAll()).size();
        assertEquals(3, count);
    }

    @Test
    public void testFindById() {
        Achievement tester = this.achievementService.findAchievementById(2).get();
        assertEquals("Newcomer", tester.getName());
        assertEquals("Play your first game", tester.getDescription());
    }

}
