package org.springframework.ntfh.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserController;
import org.springframework.ntfh.entity.user.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class UserControllerTesthowtodoinjava {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Test
    public void testFindAll() {
        // List<User> users = new ArrayList<>();
        User antonio = new User();
        antonio.setUsername("antonio");
        antonio.setPassword("antonio");
        antonio.setEmail("antonio@mail.com");
        // users.add(antonio);

        when(userService.findUser("username")).thenReturn(antonio);

        assertEquals("antonio", userService.findUser("username").getUsername());
    }
    
}
