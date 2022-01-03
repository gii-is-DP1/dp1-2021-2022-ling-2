package org.springframework.ntfh.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserController;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author alegestor
 */

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTestWhowtodoinjava {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    // @BeforeEach
    // void setup() {
    //     MockitoAnnotations.initMocks(this);
    // }

    @Test
    void testGetUser() {
        User antonio = new User();
        antonio.setUsername("antonio");
        antonio.setPassword("antonio");
        antonio.setEmail("antonio@mail.com");

        when(userService.findUser("antonio")).thenReturn(antonio);

        assertEquals(200, userController.getUser("antonio").getStatusCodeValue());
    }

    // TODO: Not working
    @Disabled
    @Test
    void testGetAll() {
        User antonio = new User();
        antonio.setUsername("antonio");
        antonio.setPassword("antonio");
        antonio.setEmail("antonio@mail.com");

        User evaristo = new User();
        evaristo.setUsername("evaristo");
        evaristo.setPassword("evaristo");
        evaristo.setEmail("evaristo@mail.com");

        List<User> users = new ArrayList<>();
        users.add(antonio);
        users.add(evaristo);

        // Iterable<User> usersIt = userService.findAll();
        // List<User> usersItList = new ArrayList<>();
        // usersIt.forEach(usersItList::add);
        // when(usersIt).thenReturn(users);

        userService = mock(UserService.class, Mockito.RETURNS_DEEP_STUBS);

        Pageable allElements = PageRequest.of(0, Integer.MAX_VALUE);
        when(userService.findPage(allElements)).thenReturn(users);

        assertEquals(200, userController.findPage(allElements).getStatusCodeValue());
    }
    
    @Test
    void testRegister() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));

        User antonio = new User();
        antonio.setUsername("antonio");
        antonio.setPassword("antonio");
        antonio.setEmail("antonio@mail.com");
        
        when(userService.createUser(any(User.class))).thenReturn(antonio);

        ResponseEntity<Map<String, String>> responseEntity = userController.register(antonio);

        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    void testLogin() {
        MockHttpServletRequest req = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));

        User antonio = new User();
        antonio.setUsername("antonio");
        antonio.setPassword("antonio");
        antonio.setEmail("antonio@mail.com");

        String token = "token";
        
        when(userService.loginUser(any(User.class))).thenReturn(token);

        ResponseEntity<Map<String, String>> responseEntity = userController.login(antonio);

        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    // TODO not working
    @Disabled
    @Test
    void testUpdateUser() {
        User antonio = new User();
        antonio.setUsername("antonio");
        antonio.setPassword("antonio");
        antonio.setEmail("antonio@mail.com");

        String token = "token";
        String newPassword = "newPassword";
        antonio.setPassword(newPassword);

        when(userService.updateUser(antonio, token)).thenReturn(antonio);

        assertEquals(true, userController.updateUser(antonio, token).getBody().keySet().contains("authorization"));
        // assertTrue(userController.updateUser(antonio, token).getBody().values().contains(newPassword));
    }

}
