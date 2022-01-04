package org.springframework.ntfh.user;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.turn.concretestates.EnemyState;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.entity.turn.concretestates.RefreshState;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.entity.user.authorities.Authorities;
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
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class, EnemyState.class, RefreshState.class })

public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Number of users in the DB
    private final Integer INITIAL_COUNT = 16;

    private User currentUser;

    @BeforeEach
    public void createUser() {
        User tester = new User();
        Set<Authorities> userAuthority = userService.findUser("user1").getAuthorities();
        tester.setUsername("antonio");
        tester.setPassword("antonio");
        tester.setEmail("antonio@mail.com");
        tester.setAuthorities(userAuthority);
        currentUser = userService.createUser(tester);
    }

    @AfterEach
    public void deleteUser() {
        userService.deleteUser(currentUser);
    }

    @Test
    public void testPH3E1() {
        List<User> PetitionUsers = new ArrayList<>();
        List<User> RepositoryUsers = new ArrayList<>();
        userService.findAll().forEach(x -> PetitionUsers.add(x));
        userRepository.findAll().forEach(y -> RepositoryUsers.add(y));
        assertEquals(RepositoryUsers.size(), PetitionUsers.size());
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(userService.findAll()).size();
        assertEquals(INITIAL_COUNT + 1, count);
    }

    @Test
    public void testfindById() {
        User tester = this.userService.findUser("stockie");
        assertTrue(passwordEncoder.matches("stockie", tester.getPassword()));
        assertEquals("stockie@mail.com", tester.getEmail());
    }

    @Test
    public void testSaveUser() {
        // User created in the BeforeEach
        User tester = currentUser;
        assertEquals("antonio", tester.getUsername());
        assertEquals("antonio@mail.com", tester.getEmail());
        assertTrue(passwordEncoder.matches("antonio", tester.getPassword()));
    }

    @Test
    public void testUpdateUser() {
        User tester = currentUser;
        String testerToken = TokenUtils.generateJWTToken(tester);
        String newPassword = "newPassword";
        tester.setPassword(newPassword);
        User updatedTester = userService.updateUser(tester, testerToken);
        assertTrue(passwordEncoder.matches(newPassword, updatedTester.getPassword()));
    }

}
