package org.springframework.ntfh.user;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @see https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/
 * @see https://www.tutorialspoint.com/spring_boot/spring_boot_rest_controller_unit_test.htm
 * @see https://dimitr.im/testing-your-rest-controllers-and-clients-with-spring
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    static User user1, user2, user3, user4;

    @BeforeAll
    static void setup() {
        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("user1");
        user1.setEmail("user1@mail.com");
        user1.setEnabled(true);

        user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("user2");
        user2.setEmail("user2@mail.com");
        user2.setEnabled(true);

        user3 = new User();
        user3.setUsername("user3");
        user3.setPassword("user3");
        user3.setEmail("user3@mail.com");
        user3.setEnabled(true);

        user4 = new User();
        user4.setUsername("user4");
        user4.setPassword("user4");
        user4.setEmail("user4");
        user4.setEnabled(true);
    }

    @Test
    void findPage_success() throws Exception {
        // Mock the userService.findPage() method
        Pageable page = PageRequest.of(1, 2);
        when(userService.findPage(page)).thenReturn(List.of(user3, user4));

        mockMvc.perform(get("/users")
                .param("page", "1")
                .param("size", "2")
                .header("authorization",
                        "Bearer " + TokenUtils.ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("user3")))
                .andExpect(jsonPath("$[1].username", is("user4")));
    }

    @Test
    void findByUsername_success() throws Exception {
        // Mock the userService.findByUsername() method
        when(userService.findUser("user1")).thenReturn(user1);

        final String USERNAME = "user1";

        mockMvc.perform(get("/users/" + USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("user1")))
                .andExpect(jsonPath("$.email", is("user1@mail.com")));
    }

    @Test
    void register_success() throws Exception {
        final String POST_JSON = "{\"username\":\"testUser\",\"email\":\"testUser@mail.com\",\"password\":\"testUser\"}";
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(POST_JSON))
                .andExpect(status().isCreated());
    }
}