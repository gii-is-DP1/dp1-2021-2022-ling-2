package org.springframework.ntfh.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 * @author alegestor
 */

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Number of users in the DB
    private final Integer INITIAL_COUNT = 17;

    private User currentUser;

    @BeforeEach
    void createUser() {
        User tester = new User();
        tester.setUsername("antonio");
        tester.setPassword("antonio");
        tester.setEmail("antonio@mail.com");
        userService.saveUser(tester);

        currentUser = tester;
    }

    @AfterEach
    void deleteUser() {
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
        User tester = this.userService.findUser("stockie").orElse(null);
        assertEquals("stockie", tester.getPassword());
        assertEquals("stockie@mail.com", tester.getEmail());
    }

    @Test
    public void testSaveUser() {
        // User created in the BeforeEach
        User tester = currentUser;
        assertEquals("antonio", tester.getUsername());
        assertEquals("antonio", tester.getPassword());
        assertEquals("antonio@mail.com", tester.getEmail());
    }

    @Test
    public void testUpdateUser() {
        User tester = currentUser;
        String prePassword = tester.getPassword();
        String posPassword = prePassword.concat("123");
        tester.setPassword(posPassword);
        assertEquals(posPassword, tester.getPassword());
    }

}
