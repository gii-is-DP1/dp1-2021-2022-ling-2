package org.springframework.samples.ntfh.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    public void testSaveUser() {
        User user4testing = new User();
        user4testing.setUsername("antonio");
        user4testing.setPassword("antonio");
        user4testing.setEmail("antonio@mail.com");
        userService.saveUser(user4testing);
        assertEquals("antonio", userService.findUser(user4testing.getUsername()).orElse(null).getUsername());
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(userService.findAll()).size();
        assertEquals(4, count);
    }

    @Test
    public void testfindById() {
        User tester = this.userService.findUser("stockie").orElse(null);
        assertEquals("stockie", tester.getPassword());
        assertEquals("stockie@mail.com", tester.getEmail());
    }
    
    @Test
    public void testFindUserPublic() {
  //      Map<String, String> user = userService.findUserPublic("andres");
        HashMap<String, String> user = new HashMap<>();
        user.put("username", userService.findUserPublic("andres").get("username"));
        user.put("email", userService.findUserPublic("andres").get("email"));
        assertEquals("andres", user.get("username"));
        assertEquals("andres@mail.com", user.get("email"));
    }

    @Test
    public void testUpdateUser() {
        User user = this.userService.findUser("alex").orElse(null);
        String preEmail = user.getEmail();
        String posEmail = preEmail.concat("XYZ");
        user.setEmail(posEmail);
        assertEquals(posEmail, user.getEmail());
    }
/*
TODO
    @Test
    public void testUpdateUserV2() {
        User user = this.userService.findUser("alex").orElse(null);
        String token = TokenUtils.generateJWTToken(user);
        User user4testing = new User();
        user4testing.setUsername("alex");
        user4testing.setPassword("alex1");
        user4testing.setEmail("alex@mail.com");
        userService.saveUser(user4testing);
    }
*/
/*
TODO
    @Test
    public void testLoginUser() {

    }
*/

    // @AfterAll
    // Deletear todo lo generado
    // private void deleteToken() {
    // null;
    // }

}
