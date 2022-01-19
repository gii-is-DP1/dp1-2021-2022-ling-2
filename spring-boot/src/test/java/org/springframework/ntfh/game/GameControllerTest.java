package org.springframework.ntfh.game;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
public class GameControllerTest {

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

	}

	@Test
	@WithMockUser("user")
	void testGetAllGames() throws Exception {
		mockMvc.perform(get("/games").header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[1].id", is(2)));
	}

	// TODO
	@Test
	@Disabled
	@WithMockUser("user")
	void testCreateGame() throws Exception {
		final String POST_JSON =
				"{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2,\"users\":[{\"username\":\"user2\",\"email\":\"user2@mail.com\",\"enabled\":true,\"lobby\":{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2},\"player\":null,\"character\":{\"id\":5,\"baseHealth\":3,\"characterTypeEnum\":\"WARRIOR\",\"characterGenderEnum\":\"MALE\",\"proficiencies\":[{\"id\":2,\"proficiencyTypeEnum\":\"MELEE\",\"secondaryDebuff\":0}]},\"authorities\":[{\"id\":16,\"authority\":\"user\"}]},{\"username\":\"user1\",\"email\":\"user1@mail.com\",\"enabled\":true,\"lobby\":{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2},\"player\":null,\"character\":{\"id\":1,\"baseHealth\":3,\"characterTypeEnum\":\"RANGER\",\"characterGenderEnum\":\"MALE\",\"proficiencies\":[{\"id\":4,\"proficiencyTypeEnum\":\"RANGED\",\"secondaryDebuff\":0},{\"id\":3,\"proficiencyTypeEnum\":\"MELEE\",\"secondaryDebuff\":-1}]},\"authorities\":[{\"id\":15,\"authority\":\"user\"}]}],\"leader\":null,\"host\":{\"username\":\"user1\",\"email\":\"user1@mail.com\",\"enabled\":true,\"lobby\":{\"id\":4,\"name\":\"user1's game\",\"game\":null,\"hasScenes\":false,\"spectatorsAllowed\":false,\"maxPlayers\":2},\"player\":null,\"character\":{\"id\":1,\"baseHealth\":3,\"characterTypeEnum\":\"RANGER\",\"characterGenderEnum\":\"MALE\",\"proficiencies\":[{\"id\":4,\"proficiencyTypeEnum\":\"RANGED\",\"secondaryDebuff\":0},{\"id\":3,\"proficiencyTypeEnum\":\"MELEE\",\"secondaryDebuff\":-1}]},\"authorities\":[{\"id\":15,\"authority\":\"user\"}]}}";
		mockMvc.perform(post("/games").contentType(MediaType.APPLICATION_JSON)
				.header("authorization", "Bearer " + TokenUtils.ADMIN_TOKEN).content(POST_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser("user")
	void testGameCount() throws Exception {
		mockMvc.perform(get("/games/count").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", is(2)));
	}


	@Test
	@WithMockUser("user")
	void testGetGame() throws Exception {
		mockMvc.perform(get("/games/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)));
	}

}
