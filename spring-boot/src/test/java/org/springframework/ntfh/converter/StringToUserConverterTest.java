package org.springframework.ntfh.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.converters.StringToUserConverter;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class, StringToUserConverter.class})
class StringToUserConverterTest {
    
    @Autowired
    private StringToUserConverter converter;

    @Autowired
    private UserService userService;

    @Test
    void testConvert_Username_Success() {
        User tester = converter.convert("user1");

        assertThat(tester).isNotNull().isEqualTo(userService.findByUsername("user1"));
    }

    @Test
    void testConvert_Usertoken_Success() {
        String token = "Bearer " + TokenUtils.generateJWTToken(userService.findByUsername("user1"));
        User tester = converter.convert(token);

        assertThat(tester).isNotNull().isEqualTo(userService.findByUsername("user1"));
    }

    @Test
    void testConvert_Failure() {
        assertThrows(DataAccessException.class, () -> converter.convert("user8000"));
    }

}
