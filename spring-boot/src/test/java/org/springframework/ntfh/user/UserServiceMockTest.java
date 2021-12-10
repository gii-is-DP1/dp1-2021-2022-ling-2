package org.springframework.ntfh.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.ntfh.entity.user.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceMockTest {
    
    @Mock
    private UserRepository userRepository;

    protected UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository);
    }

    @Test
    void testSaveUser() {
        User tester = new User();
        tester.setUsername("antonio");
        tester.setPassword("antonio");
        tester.setEmail("antonio@mail.com");

        Collection<User> sampleUsers = new ArrayList<User>();
        sampleUsers.add(tester);
        when(userRepository.findAll()).thenReturn(sampleUsers);

        Collection<User> users = (Collection<User>) this.userService.findAll();

        assertEquals(1, users.size());
        User user = users.iterator().next();
        assertEquals("antonio", user.getUsername());
        assertEquals("antonio", user.getPassword());
        assertEquals("antonio@mail.com", user.getEmail());
    }

    @Test
    public void testUpdateUser() {
        User tester = new User();
        tester.setUsername("antonio");
        tester.setPassword("antonio");
        tester.setEmail("antonio@mail.com");

        Collection<User> sampleUsers = new ArrayList<User>();
        sampleUsers.add(tester);
        when(userRepository.findAll()).thenReturn(sampleUsers);

        Collection<User> users = (Collection<User>) this.userService.findAll();

        User user = users.iterator().next();
        String prePassword = user.getPassword();
        String posPassword = prePassword.concat("123");
        user.setPassword(posPassword);
        assertEquals(posPassword, user.getPassword());
      
    }
    
}
