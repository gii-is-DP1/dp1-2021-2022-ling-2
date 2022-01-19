package org.springframework.ntfh.playablecard.abilitycard;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class AbilityCardServiceTest {

    @Autowired
    AbilityCardService abilityCardService;

    private Integer INITIAL_ABILITYCARD_COUNT = 69;

    @Test
    void testCount() {
        Integer testerInt = abilityCardService.count();

        assertThat(testerInt).isEqualTo(INITIAL_ABILITYCARD_COUNT);
    }

    @Test
    void testFindById() {
        AbilityCard tester = abilityCardService.findById(4);

        assertThat(tester.getAbilityCardTypeEnum()).isEqualTo(AbilityCardTypeEnum.DISPARO_RAPIDO);
        assertThat(tester.getCharacterTypeEnum()).isEqualTo(CharacterTypeEnum.RANGER);
    }

    @Test
    void testFindAll() {
        Integer testerInt = Lists.newArrayList(abilityCardService.findAll()).size();

        assertThat(testerInt).isEqualTo(INITIAL_ABILITYCARD_COUNT);
    }

    @Test
    void testFindByCharacterTypeEnum() {
        List<AbilityCard> testerList =
                Lists.newArrayList(abilityCardService.findByCharacterTypeEnum(CharacterTypeEnum.RANGER));

        assertThat(testerList.contains(abilityCardService.findById(1))).isTrue();
        assertThat(testerList.contains(abilityCardService.findById(2))).isTrue();
        assertThat(testerList.contains(abilityCardService.findById(4))).isTrue();
        assertThat(testerList.contains(abilityCardService.findById(10))).isTrue();
        assertThat(testerList.contains(abilityCardService.findById(11))).isTrue();
        assertThat(testerList.contains(abilityCardService.findById(13))).isTrue();
        assertThat(testerList.contains(abilityCardService.findById(15))).isTrue();
    }

    @Test
    void testFindByAbilityCardTypeEnum() {
        AbilityCard tester = abilityCardService.findByAbilityCardTypeEnum(AbilityCardTypeEnum.POCION_CURATIVA);

        assertThat(tester.getAbilityCardTypeEnum()).isEqualTo(AbilityCardTypeEnum.POCION_CURATIVA);
    }

}
