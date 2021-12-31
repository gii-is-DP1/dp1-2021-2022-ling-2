package org.springframework.ntfh.user;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import org.springframework.ntfh.entity.user.authorities.Authorities;
import org.springframework.ntfh.entity.user.authorities.AuthoritiesService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author alegestor
 * Descartado porq se usa para controllers, NO RESTCONTROLLER
 */

// @RunWith(SpringRunner.class)
// @SpringBootTest
// @AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class UserControllerTestDP {
    
    private static final String TESTER = "antonio";

    @MockBean
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    private MockMvc mockMvc;

    private User antonio;

    @BeforeEach
    void setup() {
        antonio = new User();
        antonio.setUsername(TESTER);
        antonio.setPassword("antonio");
        antonio.setEmail("antonio@mail.com");
        // authoritiesService.saveAuthorities(TESTER, "user");
        given(this.userService.findUser(TESTER)).willReturn(antonio);
    }
    
    @WithMockUser(value = "spring")
    @Test
    void testGetByUsername() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attributeExists("user"));
    }
}
