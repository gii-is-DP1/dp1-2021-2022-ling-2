package org.springframework.ntfh.game;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameController;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.player.PlayerService;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameController.class)
class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private TurnService turnService;

    @MockBean
    private DataSource dataSource;

    @BeforeEach
    void setup() {
        Game game1 = new Game();
        game1.setId(1);

        Game game2 = new Game();
        game2.setId(2);

        when(gameService.findAll()).thenReturn(List.of(game1, game2));
        when(gameService.createGame(game1)).thenReturn(game1);
        when(gameService.gameCount()).thenReturn(List.of(game1, game2).size());
        when(gameService.findGameById(1)).thenReturn(game1);
        when(gameService.startGame(1)).thenReturn(game1);
    }

    @Test
    @WithMockUser("user")
    void testGetAllGames() throws Exception {
        mockMvc.perform(get("/games").header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    @WithMockUser("admin")
    void testCreateGame() throws Exception {
        final String POST_JSON =
                "{\"name\":\"admin's game\",\"maxPlayers\":4,\"hasScenes\":false,\"spectatorsAllowed\":true}";
        mockMvc.perform(post("/games/new").with(csrf()).header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN)
                .contentType(MediaType.APPLICATION_JSON).content(POST_JSON)).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser("user")
    void testStartGame_NotEnoughPlayers() throws Exception {
        final String POST_JSON =
                "{\"id\":1,\"name\":\"user1's game\",\"startTime\":null,\"finishTime\":null,\"hasScenes\":false,\"spectatorsAllowed\":true,\"maxPlayers\":4,\"players\":[{\"id\":29,\"user\":{\"username\":\"user3\"},\"glory\":0,\"kills\":0,\"gold\":0,\"wounds\":0,\"guard\":0,\"character\":null,\"hand\":[],\"abilityPile\":[],\"discardPile\":[],\"characterTypeEnum\":null,\"dead\":false,\"turnOrder\":1},{\"id\":28,\"user\":{\"username\":\"user1\"},\"glory\":0,\"kills\":0,\"gold\":0,\"wounds\":0,\"guard\":0,\"character\":null,\"hand\":[],\"abilityPile\":[],\"discardPile\":[],\"characterTypeEnum\":null,\"dead\":false,\"turnOrder\":0}],\"leader\":{\"id\":28,\"user\":{\"username\":\"user1\"},\"glory\":0,\"kills\":0,\"gold\":0,\"wounds\":0,\"guard\":0,\"character\":null,\"hand\":[],\"abilityPile\":[],\"discardPile\":[],\"game\":{\"id\":1,\"name\":\"user1's game\",\"stateType\":\"LOBBY\",\"hasStarted\":false,\"hasFinished\":false,\"duration\":null},\"characterTypeEnum\":null,\"dead\":false,\"turnOrder\":0},\"winner\":null,\"enemiesInPile\":[],\"enemiesFighting\":[],\"marketCardsInPile\":[],\"marketCardsForSale\":[],\"comments\":[],\"stateType\":\"LOBBY\",\"currentTurn\":null,\"hasStarted\":false,\"hasFinished\":false,\"duration\":null}";
        mockMvc.perform(post("/games/1/start").with(csrf()).header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN)
                .contentType(MediaType.APPLICATION_JSON).content(POST_JSON)).andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser("user")
    void testGameCount() throws Exception {
        mockMvc.perform(get("/games/count")).andExpect(status().isOk()).andExpect(jsonPath("$", is(2)));
    }


    @Test
    @WithMockUser("user")
    void testGetGame() throws Exception {
        mockMvc.perform(get("/games/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
    }

}
