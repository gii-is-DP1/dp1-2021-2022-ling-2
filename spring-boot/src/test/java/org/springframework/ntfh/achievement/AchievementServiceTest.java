package org.springframework.ntfh.achievement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.achievement.Achievement;
import org.springframework.ntfh.entity.achievement.AchievementService;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class AchievementServiceTest {

    @Autowired
    private AchievementService achievementService;

    @Test
    void testCountWithInitialData() {
        Integer count = achievementService.achievementCount();

        assertEquals(7, count);
    }

    @Test
    void testfindAll() {
        Integer count = Lists.newArrayList(achievementService.findAll()).size();

        assertEquals(7, count);
    }

    @Test
    void testFindById() {
        Achievement tester = this.achievementService.findById(2);
        assertEquals("Newcomer", tester.getName());
        assertEquals("Play your first game", tester.getDescription());
    }

}
