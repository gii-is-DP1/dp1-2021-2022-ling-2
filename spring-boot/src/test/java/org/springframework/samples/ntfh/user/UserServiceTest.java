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
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testPH3E1(){
        List<User> PetitionUsers = new ArrayList<>();
        List<User> RepositoryUsers = new ArrayList<>();
        userService.findAll().forEach(x->PetitionUsers.add(x));
        userRepository.findAll().forEach(y->RepositoryUsers.add(y));
        assertEquals(RepositoryUsers.size(), PetitionUsers.size());
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
        User tester = new User();
        tester.setUsername("antonio");
        tester.setPassword("antonio");
        tester.setEmail("antonio@mail.com");
        userService.saveUser(tester);
        assertEquals("antonio", userService.findUser(tester.getUsername()).orElse(null).getUsername());
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(userService.findAll()).size();
        assertEquals(13, count);
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
