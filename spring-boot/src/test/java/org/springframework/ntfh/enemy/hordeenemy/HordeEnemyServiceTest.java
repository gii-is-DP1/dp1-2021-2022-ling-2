package org.springframework.ntfh.enemy.hordeenemy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.EnemyType;
import org.springframework.ntfh.entity.enemy.EnemyModifierType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(BCryptPasswordEncoder.class)
@Disabled
public class HordeEnemyServiceTest {

    @Autowired
    private EnemyService hordeEnemyService;

    @Test
    public void testCountWithInitialData() {
        // TODO: Delete all and create mock initial data. Then test count.
        // By doing this we will make this test independent of the initial data.
        Integer count = hordeEnemyService.hordeEnemyCount();
        assertEquals(27, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(hordeEnemyService.findAll()).size();
        assertEquals(27, count);
    }

    @Test
    public void testfindById() {
        Enemy tester = this.hordeEnemyService.findHordeEnemyById(17).orElse(null);
        assertEquals(EnemyType.REGEN, tester.getHordeEnemyType().getHordeEnemyTypeEnum());
        assertEquals(0, tester.getGold());
        assertEquals(0, tester.getExtraGlory());
        assertEquals(EnemyModifierType.HEALING_CAPABILITIES,
                tester.getHordeEnemyType().getHordeEnemyModifierTypeEnum());
        assertEquals(3, tester.getHordeEnemyType().getEndurance());
    }

}
