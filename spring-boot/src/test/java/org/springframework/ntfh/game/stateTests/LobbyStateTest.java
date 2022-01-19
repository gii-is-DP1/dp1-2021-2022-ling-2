package org.springframework.ntfh.game.stateTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.game.concretestates.LobbyState;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCard;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class LobbyStateTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private AbilityCardService abilityCardService;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private MarketCardService marketCardService;

    @Autowired
    private MarketCardIngameService marketCardIngameService;

	protected Game gameTester;

    protected Player ranger;

	protected Player rogue;

    protected LobbyState lobbyState;

    protected GameStateType actualState;

    protected User user1, user2, user3;

    @BeforeEach
	void init() {
		gameTester = new Game();
		gameTester.setName("test game");
		gameTester.setHasScenes(false);
		gameTester.setSpectatorsAllowed(false);
		gameTester.setMaxPlayers(2);
		gameTester.setStateType(GameStateType.LOBBY);
		gameTester = gameService.save(gameTester);

        user1 = userService.findUser("user1");
		user2 = userService.findUser("user2");
        user3 = userService.findUser("user3"); //User used for joining users test

		gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
		gameTester = gameService.joinGame(gameTester, user2);

		ranger = gameTester.getPlayers().get(0);
		rogue = gameTester.getPlayers().get(1);

		Character rangerCharacter = characterService.findById(2);
		Character rogueCharacter = characterService.findById(4);

		ranger.setCharacter(rangerCharacter);
		rogue.setCharacter(rogueCharacter);

        lobbyState = (LobbyState) gameService.getState(gameTester);

        actualState = gameTester.getStateType();
    }

	@AfterEach
	void teardown() {
		try{
            gameService.delete(gameTester);
        } catch (Exception e) {}
	}

    @Test
    void testGetNextState() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);
        assertThat(lobbyState.getNextState()).isEqualTo(GameStateType.ONGOING);
    }

    @Test
    void testDeleteGame() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);
        assertThat(gameTester).isNotNull();
        
        lobbyState.deleteGame(gameTester.getId());

        assertThrows(DataAccessException.class, () -> gameService.findGameById(gameTester.getId()));
    }
    
    @Test
    void testJoinGame_Success() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        gameTester.setMaxPlayers(3);
        lobbyState.joinGame(gameTester, user3);

        assertThat(gameTester.getPlayers().size()).isEqualTo(3);
    }

    @Test
    void testJoinGame_Failure_Player_Already_Inside() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        gameTester.setMaxPlayers(3);
        lobbyState.joinGame(gameTester, user3);

        assertThat(gameTester.getPlayers().size()).isEqualTo(3);

        assertThrows(IllegalArgumentException.class, () -> lobbyState.joinGame(gameTester, user3));
    }

    @Test
    void testJoinGame_Failure_Full_Lobby() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        assertThrows(MaximumLobbyCapacityException.class, () -> lobbyState.joinGame(gameTester, user3));
    }

    @Test
    void testRemovePlayer() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        gameTester.setMaxPlayers(3);
        lobbyState.joinGame(gameTester, user3);

        assertThat(gameTester.getPlayers().size()).isEqualTo(3);

        lobbyState.removePlayer(gameTester.getId(), user3.getUsername());

        assertThat(gameTester.getPlayers().size()).isEqualTo(2);
    }

    @Test
    void testPlayingCards_Failure() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        Player warrior = ranger;
        warrior.setCharacter(characterService.findById(5));
        AbilityCard pasoAtras = abilityCardService.findById(27);
        AbilityCardIngame pasoAtrasIngame = abilityCardIngameService.createFromAbilityCard(pasoAtras, gameTester.getLeader());
        String token = TokenUtils.generateJWTToken(warrior.getUser());

        assertThrows(IllegalStateException.class, () -> lobbyState.playCard(pasoAtrasIngame.getId(), null, token));
    }

    @Test
    void testBuyingCards_Failure() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        MarketCard capaElfica = marketCardService.findMarketCardById(11).get();
        MarketCardIngame capaElficaIngame = marketCardIngameService.createFromMarketCard(capaElfica, gameTester);
        String token = TokenUtils.generateJWTToken(ranger.getUser());

        assertThrows(IllegalStateException.class, () -> lobbyState.buyMarketCard(capaElficaIngame.getId(), token));
    }

    @Test
    void testStartGame() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        lobbyState.startGame(gameTester.getId());
        GameStateType newActualState = gameTester.getStateType();

        assertThat(newActualState).isEqualTo(GameStateType.ONGOING);
    }

    @Test
    void testFinishGame_Failure() {
        assertThat(actualState).isEqualTo(GameStateType.LOBBY);

        assertThrows(IllegalStateException.class, () -> lobbyState.finishGame(gameTester));
    }

}
