package org.springframework.ntfh.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

        List<User> userList = new ArrayList<>();
        
        Collection<User> users = this.userService.findAll().forEach(action);
        assertEquals(1, users.size());

        // assertThat(vets).hasSize(1);
        // Vet vet = vets.iterator().next();
        // assertThat(vet.getLastName()).isEqualTo("Douglas");
        // assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
        // //Create sample User object and set all the properties
        // when(userRepository.save(Mockito.any(User.class))).thenReturn(tester);
        // Assert.assertEquals("antonio", tester.getUsername());



    }
    
}
