package org.springframework.ntfh.proficiency;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyService;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class ProficiencyServiceTest {

    @Autowired
    private ProficiencyService proficiencyService;

    @Test
    void testCountWithInitialData() {
        Integer counter = proficiencyService.count();
        assertEquals(6, counter);
    }

    @Test
    void testFindAll() {
        Integer counter = Lists.newArrayList(proficiencyService.findAll()).size();
        assertEquals(6, counter);
    }

    @Test
    void testFindProficiencyById() {
        Proficiency tester = proficiencyService.findProficiencyById(2).get();
        assertEquals(ProficiencyTypeEnum.MELEE, tester.getProficiencyTypeEnum());
    }
    
}
