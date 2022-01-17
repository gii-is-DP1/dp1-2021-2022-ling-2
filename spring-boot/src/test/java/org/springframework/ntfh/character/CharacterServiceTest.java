package org.springframework.ntfh.character;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterGenderEnum;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({BCryptPasswordEncoder.class, PlayerState.class, MarketState.class})
public class CharacterServiceTest {

    @Autowired
    private CharacterService characterService;

    @Test
    public void testfindById() {
        Character tester = this.characterService.findById(2);
        assertThat(tester.getCharacterTypeEnum()).isEqualTo(CharacterTypeEnum.RANGER);
        assertThat(tester.getCharacterGenderEnum()).isEqualTo(CharacterGenderEnum.FEMALE);
    }

}
