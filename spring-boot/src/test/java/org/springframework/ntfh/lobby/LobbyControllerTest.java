package org.springframework.ntfh.lobby;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.lobby.LobbyService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LobbyControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @MockBean
    LobbyService lobbyService;

    static Lobby lobby1, lobby2, lobby3, lobby4;

    static User user1, user2;

    @BeforeAll
    static void setup() {

        lobby1 = new Lobby();
        lobby1.setId(1);
        lobby1.setName("lobby1");

        lobby2 = new Lobby();
        lobby2.setId(2);
        lobby2.setName("lobby2");

        user1 = new User();
        user1.setUsername("user1");

        user2 = new User();
        user2.setUsername("user2");
    }

    @Test
    void testGetAllLobbies() throws Exception {
        when(lobbyService.findAll()).thenReturn(List.of(lobby1, lobby2));
        mockMvc.perform(get("/lobbies")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("lobby1")))
        .andExpect(jsonPath("$[1].name", is("lobby2")));
    }

    @Test
    void testLobbyCount() throws Exception {
        when(lobbyService.lobbyCount()).thenReturn(List.of(lobby1, lobby2).size());
        mockMvc.perform(get("/lobbies/count")).andExpect(status().isOk()).andExpect(jsonPath("$", is(2)));
    }

    @Test
    void testLobbyStatus() throws Exception {
        when(lobbyService.findLobby(1)).thenReturn(lobby1);
        mockMvc.perform(get("/lobbies/1")).andExpect(status().isOk()).andExpect(jsonPath("$.name", is("lobby1")));
    }

    // TODO wait until the refactor of the method in lobbycontroller is made
    @Disabled
    @Test
    void testJoinLobby() throws Exception {
        final String POST_JSON = "{\"username\":\"testUser\",\"email\":\"testUser@mail.com\",\"password\":\"testUser\"}";
        mockMvc.perform(post("/lobbies/1/join").contentType(MediaType.APPLICATION_JSON).header("authorization",
                "Bearer " + TokenUtils.ADMIN_TOKEN)
                .content(POST_JSON))
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
