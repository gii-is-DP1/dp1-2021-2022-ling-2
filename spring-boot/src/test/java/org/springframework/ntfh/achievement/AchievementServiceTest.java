package org.springframework.ntfh.achievement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.entity.achievement.Achievement;
import org.springframework.ntfh.entity.achievement.AchievementService;
import org.springframework.ntfh.entity.achievement.AchievementType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class AchievementServiceTest {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private UserService userService;

    protected Integer INITIAL_ACHIEVEMENT_COUNT = 14;

    protected Achievement achievementTester, newAchievement;

    protected User user1;

    @BeforeEach
    void init() {
        achievementTester = achievementService.findById(2);
        user1 = userService.findByUsername("user1");

        Achievement achievementCreated = new Achievement();
        achievementCreated.setName("Sacrifice to the devil");
        achievementCreated.setDescription("Kill an amount of 666 enemies");
        achievementCreated.setType(AchievementType.KILL_X_ENEMIES);
        achievementCreated.setCondition(666);
        achievementService.createAchievement(achievementCreated);
        newAchievement = achievementCreated;
    }

    @AfterEach
    void teardown() {
        try {
            achievementService.delete(newAchievement);
        } catch (Exception e) {}
    }

    @Test
    void testCountWithInitialData() {
        Integer count = achievementService.count();

        assertThat(count).isEqualTo(INITIAL_ACHIEVEMENT_COUNT+1);
    }

    @Test
    void testfindAll() {
        Integer count = Lists.newArrayList(achievementService.findAll()).size();

        assertThat(count).isEqualTo(INITIAL_ACHIEVEMENT_COUNT+1);
    }

    @Test
    void testFindPageable() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Achievement> pagedAchievements = Lists.newArrayList(achievementService.findPageable(pageable));

        assertThat(pagedAchievements).isNotEmpty();
        assertThat(pagedAchievements.get(0)).isEqualTo((achievementService.findById(1)));
        assertThat(pagedAchievements.get(1)).isEqualTo((achievementService.findById(2)));
        assertThat(pagedAchievements.get(2)).isEqualTo((achievementService.findById(3)));
    }

    @Test
    void testFindById() {
        Integer XGAMES = 1; 

        assertThat(achievementTester.getName()).isEqualTo("Newcomer");
        assertThat(achievementTester.getDescription()).isEqualTo("Play your first game");
        assertThat(achievementTester.getType()).isEqualTo(AchievementType.PLAY_X_GAMES);
        assertThat(achievementTester.getCondition()).isEqualTo(XGAMES);
    }

    @Test
    void testUpdateAchievement_Success() {
        String adminToken = TokenUtils.ADMIN_TOKEN;
        Achievement updatedAchievement = achievementService.findById(7);
        Integer updatedCondition = 11;
        updatedAchievement.setCondition(updatedCondition);
        achievementService.updateAchievement(updatedAchievement, adminToken);

        assertThat(achievementService.findById(7).getCondition()).isEqualTo(updatedCondition);
    }

    @Test
    void testUpdateAchievement_Failure_Achievement_Updated_By_NonAdmin() {
        String nonAdminToken = TokenUtils.USER_TOKEN;
        Achievement newAchievement = achievementService.findById(7);
        Integer newCondition = 11;
        newAchievement.setCondition(newCondition);
        
        assertThrows(NonMatchingTokenException.class, () -> achievementService.updateAchievement(newAchievement, nonAdminToken));
    }

    @Test
    void testFindByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Achievement> userAchievements = achievementService.findByUser(user1, pageable);
        Integer initialAchievement = 1;

        assertThat(userAchievements).hasSize(initialAchievement);
        assertThat(userAchievements.get(0)).isEqualTo(achievementService.findById(1));
    }

    @Test
    void testCountByUser() {
        Integer userAchievementInt = achievementService.countByUser(user1);
        Integer IntUserAchievement = 1;

        assertThat(userAchievementInt).isEqualTo(IntUserAchievement);
    }

    @Test
    void testDeleteAchievement() {
        achievementService.delete(newAchievement);
        Integer achievementDeleted = newAchievement.getId();

        assertThrows(DataAccessException.class, () -> achievementService.findById(achievementDeleted));
    }

    @Test
    void testFindAllTypes() {
        List<AchievementType> achievementTypes = achievementService.findAllTypes();
        Integer allTypes = 6;

        assertThat(achievementTypes).isNotEmpty().hasSize(allTypes).contains(AchievementType.CREATE_ACCOUNT).contains(AchievementType.EARN_X_GLORY).contains(AchievementType.KILL_X_ENEMIES).contains(AchievementType.PLAY_X_DISTINCT_CHARACTERS).contains(AchievementType.PLAY_X_GAMES).contains(AchievementType.WIN_X_GAMES);
    }

    @Test
    void testCreateAchivement() {
        // Creation made in the init
        String name = "Sacrifice to the devil";
        String description = "Kill an amount of 666 enemies";
        Integer condition = 666;

        assertThat(newAchievement.getName()).isEqualTo(name);
        assertThat(newAchievement.getDescription()).isEqualTo(description);
        assertThat(newAchievement.getType()).isEqualTo(AchievementType.KILL_X_ENEMIES);
        assertThat(newAchievement.getCondition()).isEqualTo(condition);
    }

}
