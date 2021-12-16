package org.springframework.ntfh.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.hamcrest.Matchers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.ntfh.configuration.SecurityConfiguration;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserController;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.entity.user.authorities.AuthoritiesService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author alegestor
 */

// @WebMvcTest(controllers=UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
// 		excludeAutoConfiguration= SecurityConfiguration.class)
// @WebMvcTest(UserController.class)
// @RunWith(SpringRunner.class)
// @SpringBootTest
// @AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;
    
    // private static final String TEST_USER_USERNAME = "antonio";

    // @Autowired
    // private MockMvc mockMvc;

    // @MockBean
    // private UserService userService;

    // @MockBean
    // private AuthoritiesService authoritiesService;

    // private User tester;

    // private static ObjectMapper mapper = new ObjectMapper();


    
    // @BeforeEach
    // void setup() {
    //     tester = new User();
    //     tester.setUsername(TEST_USER_USERNAME);
    //     tester.setPassword("antonio");
    //     tester.setEmail("antonio@mail.com");
    //     given(this.userService.findUser(TEST_USER_USERNAME)).willReturn(Optional.of(tester));
    // }

    // Error 403
    // @Test
    // void testGetByUsername() throws Exception {
    //     List<User> users = new ArrayList<>();
    //     tester = new User();
    //     tester.setUsername("antonio");
    //     tester.setPassword("antonio");
    //     tester.setEmail("antonio@mail.com");
    //     users.add(tester);
    //     Mockito.when(userService.findAll()).thenReturn(users);
    //     mockMvc.perform(get("/getMapping")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
    //             .andExpect(jsonPath("$[0].username", Matchers.equalTo("antonio")));
    // }

        @Test
        void testGetAllUsers() {
            List<User> users = new ArrayList<>();
            User antonio = new User();
            antonio.setUsername("antonio");
            antonio.setPassword("antonio");
            antonio.setEmail("antonio@mail.com");
            users.add(antonio);
            User eustaquio = new User();
            eustaquio.setUsername("eustaquio");
            eustaquio.setPassword("eustaquio");
            eustaquio.setEmail("eustaquio@mail.com");
            users.add(eustaquio);

            when(userService.findAll()).thenReturn(users);
            
            List<User> res = new ArrayList<>();
            res = userController.getUser("antonio");
        }

}
