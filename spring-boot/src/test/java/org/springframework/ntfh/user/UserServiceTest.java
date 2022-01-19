package org.springframework.ntfh.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.entity.user.authorities.Authorities;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 * @author alegestor
 * @author andrsdt
 */

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Number of users in the DB
    private final Integer INITIAL_COUNT = 16;

    private User currentUser;

    @BeforeEach
    void createUser() {
        User tester = new User();
        Set<Authorities> userAuthority = userService.findByUsername("user1").getAuthorities();
        tester.setUsername("antonio");
        tester.setPassword("antonio");
        tester.setEmail("antonio@mail.com");
        tester.setAuthorities(userAuthority);
        currentUser = userService.createUser(tester);
    }

    @Test
    void testfindById() {
        User tester = this.userService.findByUsername("stockie");

        assertThat(passwordEncoder.matches("stockie", tester.getPassword())).isTrue();
        assertThat(tester.getEmail()).isEqualTo("stockie@mail.com");
    }

    @Test
    void testSaveUser() {
        // User created in the BeforeEach
        User tester = currentUser;

        assertThat(tester.getUsername()).isEqualTo("antonio");
        assertThat(tester.getEmail()).isEqualTo("antonio@mail.com");
        assertThat(passwordEncoder.matches("antonio", tester.getPassword())).isTrue();
    }

    @Test
    void testUpdateUser() {
        User tester = currentUser;
        String testerToken = TokenUtils.generateJWTToken(tester);
        String newPassword = "newPassword";
        tester.setPassword(newPassword);
        User updatedTester = userService.updateUser(tester, testerToken);

        assertThat(passwordEncoder.matches(newPassword, updatedTester.getPassword())).isTrue();
    }

    @Test
    void testDeleteUser() {
        User tester = currentUser;
        String username = tester.getUsername();
        userService.deleteUser(tester);

        assertThrows(DataAccessException.class, () -> userService.findByUsername(username));
    }

    // H3 + E1
    @Test
    void testfindAll() {
        Integer count = Lists.newArrayList(userService.findAll()).size();

        assertThat(count).isEqualTo(INITIAL_COUNT + 1);
    }

}
