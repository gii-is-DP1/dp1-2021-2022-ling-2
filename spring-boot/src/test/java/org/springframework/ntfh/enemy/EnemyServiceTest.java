package org.springframework.ntfh.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class EnemyServiceTest {

    @Autowired
    private EnemyService enemyService;

    // Number of enemies in the DB
    private final Integer ENEMY_COUNT = 30;

    // Number of enemies in the DB
    private final Integer WARLORD_COUNT = 3;

    // Number of enemies in the DB
    private final Integer HORDE_COUNT = 27;

    @Test
    public void testCountWithInitialData() {
        // TODO: Delete all and create mock initial data. Then test count.
        // By doing this we will make this test independent of the initial data.
        Integer count = enemyService.count();
        assertEquals(ENEMY_COUNT, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(enemyService.findAll()).size();
        assertEquals(ENEMY_COUNT, count);
    }

    @Test
    public void testfindById() {
        Enemy tester = this.enemyService.findEnemyById(17).orElse(null);
        assertEquals(EnemyType.REGEN, tester.getEnemyType());
        assertEquals(0, tester.getGold());
        assertEquals(0, tester.getExtraGlory());
        assertEquals(EnemyModifierType.HEALING_CAPABILITIES,
                tester.getEnemyModifierType());
        assertEquals(3, tester.getEndurance());
        ;
    }

    @Test
    void testEnemyCategoryType() {
        List<Enemy> warlords = enemyService.findByEnemyCategoryType(EnemyCategoryType.WARLORD);
        assertEquals(WARLORD_COUNT, warlords.size());
        List<Enemy> enemies = enemyService.findByEnemyCategoryType(EnemyCategoryType.HORDE);
        assertEquals(HORDE_COUNT, enemies.size());
    }

}
