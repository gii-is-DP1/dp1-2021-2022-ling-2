package org.springframework.ntfh.playablecard.abilitycard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class AbilityCardServiceTest {

    @Autowired
    AbilityCardService abilityCardService;

    private Integer INITIAL_ABILITYCARD_COUNT = 69;

    @Test
    void testCount() {
        Integer testerInt = abilityCardService.count();
        assertEquals(INITIAL_ABILITYCARD_COUNT, testerInt);
    }

    @Test
    void testFindById() {
        AbilityCard tester = abilityCardService.findById(4);
        assertEquals(AbilityCardTypeEnum.DISPARO_RAPIDO, tester.getAbilityCardTypeEnum());
        assertEquals(CharacterTypeEnum.RANGER, tester.getCharacterTypeEnum());
    }

    @Test
    void testFindAll() {
        Integer testerInt = Lists.newArrayList(abilityCardService.findAll()).size();
        assertEquals(INITIAL_ABILITYCARD_COUNT, testerInt);
    }

    @Test
    void testFindByCharacterTypeEnum() {
        List<AbilityCard> testerList = Lists.newArrayList(abilityCardService.findByCharacterTypeEnum(CharacterTypeEnum.RANGER));
        assertEquals(true, testerList.contains(abilityCardService.findById(1)));
        assertEquals(true, testerList.contains(abilityCardService.findById(2)));
        assertEquals(true, testerList.contains(abilityCardService.findById(4)));
        assertEquals(true, testerList.contains(abilityCardService.findById(10)));
        assertEquals(true, testerList.contains(abilityCardService.findById(11)));
        assertEquals(true, testerList.contains(abilityCardService.findById(13)));
        assertEquals(true, testerList.contains(abilityCardService.findById(15)));
    }

    // TODO repasar querys, da error
    @Disabled
    @Test
    void testFindByAbilityCardTypeEnum() {
        AbilityCard tester = abilityCardService.findByAbilityCardTypeEnum(AbilityCardTypeEnum.ESPADAZO);
        assertEquals(AbilityCardTypeEnum.ESPADAZO, tester.getAbilityCardTypeEnum());
    }
    
}
