package org.springframework.ntfh.enemy;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyCategoryType;
import org.springframework.ntfh.entity.enemy.EnemyModifierType;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.EnemyType;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class EnemyServiceTest {

    @Autowired
    private EnemyService enemyService;

    // Number of enemies in the DB
    private final Integer ENEMY_COUNT = 30;

    // Number of enemies in the DB
    private final Integer WARLORD_COUNT = 3;

    // Number of enemies in the DB
    private final Integer HORDE_COUNT = 27;

    @Test
    void testCountWithInitialData() {
        Integer count = enemyService.count();

        assertThat(count).isEqualTo(ENEMY_COUNT);
    }

    @Test
    void testfindAll() {
        Integer count = Lists.newArrayList(enemyService.findAll()).size();

        assertThat(count).isEqualTo(ENEMY_COUNT);
    }

    @Test
    void testfindById() {
        Enemy tester = this.enemyService.findEnemyById(17).orElse(null);
        Integer GLORY_OF_THE_ENEMY = 2;
        Integer ENDURANCE_OF_THE_ENEMY = 3;

        assertThat(tester.getEnemyType()).isEqualTo(EnemyType.REGEN);
        assertThat(tester.getGold()).isZero();
        assertThat(tester.getBaseGlory()).isEqualTo(GLORY_OF_THE_ENEMY);
        assertThat(tester.getExtraGlory()).isZero();
        assertThat(tester.getEnemyModifierType()).isEqualTo(EnemyModifierType.HEALING_CAPABILITIES);
        assertThat(tester.getEndurance()).isEqualTo(ENDURANCE_OF_THE_ENEMY);;
    }

    @Test
    void testEnemyCategoryType() {
        List<Enemy> warlords = enemyService.findByEnemyCategoryType(EnemyCategoryType.WARLORD);

        assertThat(warlords).hasSize(WARLORD_COUNT);

        List<Enemy> enemies = enemyService.findByEnemyCategoryType(EnemyCategoryType.HORDE);

        assertThat(enemies).hasSize(HORDE_COUNT);
    }

}
