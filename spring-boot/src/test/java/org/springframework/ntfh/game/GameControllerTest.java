package org.springframework.ntfh.game;

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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameService gameService;

    static Game game1, game2;

    @BeforeAll
    static void setup() {
        game1 = new Game();
        game1.setId(1);

        game2 = new Game();
        game2.setId(2);
    }

    @Test
    void testGetAllGames() throws Exception {
        when(gameService.findAll()).thenReturn(List.of(game1, game2));
        mockMvc.perform(get("/games")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[1].id", is(2)));
    }

    // TODO
    @Test
    void testCreateGame() throws Exception {
        final String POST_JSON = "{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2,\"users\":[{\"username\":\"user2\",\"email\":\"user2@mail.com\",\"enabled\":true,\"lobby\":{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2},\"player\":null,\"character\":{\"id\":3,\"baseHealth\":2,\"characterTypeEnum\":\"ROGUE\",\"characterGenderEnum\":\"MALE\",\"proficiencies\":[{\"id\":1,\"proficiencyTypeEnum\":\"DEXTERITY\",\"secondaryDebuff\":0},{\"id\":5,\"proficiencyTypeEnum\":\"RANGED\",\"secondaryDebuff\":-1}]},\"authorities\":[{\"id\":16,\"authority\":\"user\"}]},{\"username\":\"user1\",\"email\":\"user1@mail.com\",\"enabled\":true,\"lobby\":{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2},\"player\":null,\"character\":{\"id\":7,\"baseHealth\":2,\"characterTypeEnum\":\"WIZARD\",\"characterGenderEnum\":\"MALE\",\"proficiencies\":[{\"id\":6,\"proficiencyTypeEnum\":\"SPELL\",\"secondaryDebuff\":0}]},\"authorities\":[{\"id\":15,\"authority\":\"user\"}]}],\"leader\":null,\"host\":{\"username\":\"user1\",\"email\":\"user1@mail.com\",\"enabled\":true,\"lobby\":{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2},\"player\":null,\"character\":{\"id\":7,\"baseHealth\":2,\"characterTypeEnum\":\"WIZARD\",\"characterGenderEnum\":\"MALE\",\"proficiencies\":[{\"id\":6,\"proficiencyTypeEnum\":\"SPELL\",\"secondaryDebuff\":0}]},\"authorities\":[{\"id\":15,\"authority\":\"user\"}]}}";
        mockMvc.perform(post("/games").contentType(MediaType.APPLICATION_JSON)
        .header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN)
        .content(POST_JSON))
        .andExpect(status().isCreated());
    }

    @Test
    void testGameCount() throws Exception {
        when(gameService.gameCount()).thenReturn(List.of(game1, game2).size());
        mockMvc.perform(get("/games/count")).andExpect(status().isOk()).andExpect(jsonPath("$", is(2)));
    }

    @Test
    void testGetGame() throws Exception {
        when(gameService.findGameById(1)).thenReturn(game1);
        mockMvc.perform(get("/games/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
    }



}
