package org.springframework.ntfh.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
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
        HordeEnemy tester = this.hordeEnemyService.findHordeEnemyById(17).orElse(null);
        assertEquals(HordeEnemyTypeEnum.REGEN, tester.getHordeEnemyType().getHordeEnemyTypeEnum());
        assertEquals(0, tester.getGold());
        assertEquals(0, tester.getExtraGlory());
        assertEquals(HordeModifierTypeEnum.HEALING_CAPABILITIES,
                tester.getHordeEnemyType().getHordeEnemyModifierTypeEnum());
        assertEquals(3, tester.getHordeEnemyType().getEndurance());
    }

}
