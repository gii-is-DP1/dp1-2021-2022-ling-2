package org.springframework.ntfh.lobby;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.ntfh.configuration.SecurityConfiguration;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyController;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LobbyController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = WebSecurityConfigurer.class),
        excludeAutoConfiguration = SecurityConfiguration.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {

        Lobby lobby1 = new Lobby();
        lobby1.setId(1);
        lobby1.setName("lobby1");

        Lobby lobby2 = new Lobby();
        lobby2.setId(2);
        lobby2.setName("lobby2");

        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        when(lobbyService.findAll()).thenReturn(List.of(lobby1, lobby2));
        when(lobbyService.lobbyCount()).thenReturn(List.of(lobby1, lobby2).size());
        when(lobbyService.findLobby(1)).thenReturn(lobby1);

    }

    @Test
    @WithMockUser("tester")
    void testGetAllLobbies() throws Exception {
        mockMvc.perform(get("/lobbies")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].name", is("lobby1")))
                .andExpect(jsonPath("$[1].name", is("lobby2")));
    }

    @Test
    @WithMockUser("tester")
    void testLobbyCount() throws Exception {
        mockMvc.perform(get("/lobbies/count")).andExpect(status().isOk())
                .andExpect(jsonPath("$", is(2)));
    }

    @Test
    @WithMockUser("tester")
    void testLobbyStatus() throws Exception {
        mockMvc.perform(get("/lobbies/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("lobby1")));
    }

    // TODO wait until the refactor of the method in lobbycontroller is made
    @Disabled
    @Test
    void testJoinLobby() throws Exception {
        final String POST_JSON =
                "{\"username\":\"testUser\",\"email\":\"testUser@mail.com\",\"password\":\"testUser\"}";
        mockMvc.perform(post("/lobbies/1/join").contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN).content(POST_JSON))
                .andExpect(status().isOk());
    }

    // TODO wait until the refactor of the method in lobbycontroller is made
    @Disabled
    @Test
    void testRemoveUserFromLobby() {

    }

    // TODO wait until the refactor of the method in lobbycontroller is made
    @Disabled
    @Test
    void testDeleteLobby() {

    }

}
