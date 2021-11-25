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
    // @Autowired
    // private UserController userController;

    @Test
    public void testPH3E1(){
        // User user= new User();
        // user.setUsername("andres");
        // user.setPassword("andres");
        // userController.login(user);
        List<User> l = new ArrayList<>();
        userService.findAll().forEach(x->l.add(x));
        assertEquals(2, l.size());
    }

    // public Void voidNH3E1(){
    //     User user= new User();
    //     user.setUsername("andres");
    //     user.setPassword("andres");
    //     userController.login(user);
    //     List<User> l = new ArrayList<>();
    //     userService.findAll().forEach(l.add(x)->);
    // }



}
