package org.springframework.ntfh.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyModifierType;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.EnemyType;
import org.springframework.ntfh.entity.turn.concretestates.EnemyState;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.entity.turn.concretestates.RefreshState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class, EnemyState.class, RefreshState.class })
public class EnemyServiceTest {

    @Autowired
    private EnemyService enemyService;

    @Test
    public void testCountWithInitialData() {
        // TODO: Delete all and create mock initial data. Then test count.
        // By doing this we will make this test independent of the initial data.
        Integer count = enemyService.count();
        assertEquals(30, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(enemyService.findAll()).size();
        assertEquals(30, count);
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

}
