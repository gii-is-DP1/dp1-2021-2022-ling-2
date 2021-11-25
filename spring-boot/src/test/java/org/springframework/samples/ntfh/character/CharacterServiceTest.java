package org.springframework.samples.ntfh.character;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CharacterServiceTest {

    @Autowired
    private CharacterService characterService;

    @Test
    public void testfindById() {
        Character tester = this.characterService.findCharacterById(2).get();
        assertEquals(CharacterTypeEnum.RANGER, tester.getCharacterTypeEnum());
        assertEquals(CharacterGenderEnum.FEMALE, tester.getCharacterGenderEnum());
    }
    
}
