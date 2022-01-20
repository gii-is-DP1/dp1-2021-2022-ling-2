package org.springframework.ntfh.proficiency;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.proficiency.Proficiency;
import org.springframework.ntfh.entity.proficiency.ProficiencyService;
import org.springframework.ntfh.entity.proficiency.ProficiencyTypeEnum;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class ProficiencyServiceTest {

    @Autowired
    private ProficiencyService proficiencyService;

    protected Integer ALL_PROFICIENCIES = 6;

    @Test
    void testCountWithInitialData() {
        Integer counter = proficiencyService.count();

        assertThat(counter).isEqualTo(ALL_PROFICIENCIES);
    }

    @Test
    void testFindAll() {
        Integer counter = Lists.newArrayList(proficiencyService.findAll()).size();

        assertThat(counter).isEqualTo(ALL_PROFICIENCIES);
    }

    @Test
    void testFindProficiencyById() {
        Proficiency tester = proficiencyService.findProficiencyById(2).get();

        assertThat(tester.getProficiencyTypeEnum()).isEqualByComparingTo(ProficiencyTypeEnum.MELEE);
    }

}
