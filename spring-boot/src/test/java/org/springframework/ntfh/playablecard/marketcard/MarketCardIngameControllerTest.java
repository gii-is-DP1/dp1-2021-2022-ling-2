package org.springframework.ntfh.playablecard.marketcard;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameController;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MarketCardIngameController.class)
class MarketCardIngameControllerTest {

    @MockBean
    private MarketCardIngameService marketCardIngameService;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataSource dataSource;

    @BeforeEach
    void setup() {
        Game game1 = new Game();
        game1.setId(1);

        when(marketCardIngameService.buyMarketCard(anyInt(), anyString())).thenReturn(game1);

    }

    @Test
    @WithMockUser("user1")
    void buyCard_success() throws Exception {
        final String POST_JSON = "{}";
        mockMvc.perform(
                post("/market-cards/buy/1").with(csrf()).header("authorization", "Bearer " + TokenUtils.USER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).content(POST_JSON))
                .andExpect(status().isOk());


    }


}
