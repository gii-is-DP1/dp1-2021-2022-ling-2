package org.springframework.ntfh.enemy.warlord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.enemy.warlord.Warlord;
import org.springframework.ntfh.entity.enemy.warlord.WarlordService;
import org.springframework.ntfh.entity.enemy.warlord.WarlordTypeEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import(BCryptPasswordEncoder.class)
public class WarlordServiceTest {

    @Autowired
    private WarlordService warlordService;

    @Test
    public void testCountWithInitialData() {
        // TODO: Delete all and create mock initial data. Then test count.
        // By doing this we will make this test independent of the initial data.
        Integer count = warlordService.warlordCount();
        assertEquals(3, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(warlordService.findAll()).size();
        assertEquals(3, count);
    }

    @Test
    public void testfindById() {
        Warlord tester = this.warlordService.findWarlordById(3).orElse(null);
        assertEquals(WarlordTypeEnum.SHRIEKKNIFER, tester.getWarlordTypeEnum());
        assertEquals(10, tester.getEndurance());
    }

}
