package org.springframework.ntfh.user;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserController;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.entity.user.authorities.Authorities;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @see https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/
 * @see https://www.tutorialspoint.com/spring_boot/spring_boot_rest_controller_unit_test.htm
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @BeforeAll
    public static void setup() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("user1");
        user.setEmail("user1@mail.com");
        user.setEnabled(true);

        Authorities userAuthority = new Authorities();
        userAuthority.setUser(user);
        userAuthority.setAuthority("user");
        user.setAuthorities(Set.of(userAuthority));
    }

    @Test
    void getAll_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
        .get("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is("user1")));
    }
}
