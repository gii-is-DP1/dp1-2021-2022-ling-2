package org.springframework.samples.ntfh.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UserServiceTest {
    // TODO implement when JWT authentication is ready

    @Autowired
    private UserService userService;

    @Test
    public void testPH3E1() {
        List<User> l = new ArrayList<>();
        userService.findAll().forEach(x -> l.add(x));
        assertEquals(4, l.size());
    }

    /**
     * @BeforeAll private User createUserForTesting() { User user4testing = new
     *            User(); user4testing.setUsername("alex");
     *            user4testing.setPassword("alex");
     *            user4testing.setEmail("alex@mail.com");
     *            userService.saveUser(user4testing); return user4testing; }
     * 
     * @BeforeAll private String tokenGen() { User user4testing = String userToken =
     *            TokenUtils.generateJWTToken(user4testing); return userToken; }
     */
    @Test
    public void testUpdateUser() {
        User user = this.userService.findUser("alex").orElse(null);
        String preEmail = user.getEmail();
        String posEmail = preEmail.concat("XYZ");
        user.setEmail(posEmail);
        // this.userService.saveUser(user);
        assertEquals(posEmail, user.getEmail());

        /**
         * User user4testing = createUserForTesting(); String userToken = tokenGen();
         * user4testing.setUsername("ale"); userService.updateUser(user4testing,
         * userToken); assertEquals("ale", user4testing.getUsername());
         */
    }

    // @AfterAll
    // Deletear todo lo generado
    // private void deleteToken() {
    // null;
    // }

}
