package org.springframework.ntfh.game.stateTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.game.concretestates.OngoingState;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCard;
import org.springframework.ntfh.entity.playablecard.marketcard.MarketCardService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngame;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.State;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;


@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class OngoingStateTest {

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

    @Autowired
    private TurnService turnService;

    protected Game gameTester;

    protected Player ranger;

	protected Player rogue;

    protected OngoingState ongoingState;

    protected GameStateType actualState;

    protected User user1, user2, user3;

    @BeforeEach
	public void init() {
		gameTester = new Game();
		gameTester.setName("test game");
		gameTester.setHasScenes(false);
		gameTester.setSpectatorsAllowed(false);
		gameTester.setMaxPlayers(2);
		gameTester.setStateType(GameStateType.LOBBY);
		gameTester = gameService.save(gameTester);

        user1 = userService.findByUsername("user1");
		user2 = userService.findByUsername("user2");
        user3 = userService.findByUsername("user3"); //User used for joining users test

		gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
		gameTester = gameService.joinGame(gameTester, user2);

		ranger = gameTester.getPlayers().get(0);
		rogue = gameTester.getPlayers().get(1);

		Character rangerCharacter = characterService.findById(2);
		Character rogueCharacter = characterService.findById(4);

		ranger.setCharacter(rangerCharacter);
		rogue.setCharacter(rogueCharacter);

        gameService.startGame(gameTester.getId());

        ongoingState = (OngoingState) gameService.getState(gameTester);

        actualState = gameTester.getStateType();
    }

	@AfterEach
	public void teardown() {
		try{
            gameService.delete(gameTester);
        } catch (Exception e) {}
	}

    @Test
    void testPreState() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        ongoingState.preState(gameTester);
        Integer INITIAL_NUMBER_OF_ENEMIES = 3;
        Integer INITIAL_NUMBER_OF_CARDS = 4;
        
        assertThat(gameTester.getHasStarted()).isTrue();
        assertThat(gameTester.getEnemiesFighting()).hasSize(INITIAL_NUMBER_OF_ENEMIES);
        assertThat(ranger.getHand()).hasSize(INITIAL_NUMBER_OF_CARDS);
        assertThat(rogue.getHand()).hasSize(INITIAL_NUMBER_OF_CARDS);
    }

    @Test
    void testGetNextState() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);
        assertThat(ongoingState.getNextState()).isEqualTo(GameStateType.FINISHED);
    }    

    @Test
    void testDeleteGame_Failure() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);
        assertThat(gameTester).isNotNull();

        Integer gameId = gameTester.getId();
        
        assertThrows(IllegalStateException.class, () -> ongoingState.deleteGame(gameId));
    }

    @Test
    void testJoinGame_Failure() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        gameTester.setMaxPlayers(3);
        
        assertThrows(IllegalStateException.class, () -> ongoingState.joinGame(gameTester, user3));
    }

    @Test
    void testRemovePlayer_Failure() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        Integer gameId = gameTester.getId();
        String username = user2.getUsername();

        assertThrows(IllegalStateException.class, () -> ongoingState.removePlayer(gameId, username));
    } 
    
    @Test
    void testPlayCard() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        Player warrior = ranger;
        warrior.setCharacter(characterService.findById(5));
        AbilityCard pasoAtras = abilityCardService.findById(27);
        AbilityCardIngame pasoAtrasIngame = abilityCardIngameService.createFromAbilityCard(pasoAtras, warrior);
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(pasoAtrasIngame);
        warrior.setHand(hand);
        String token = TokenUtils.generateJWTToken(warrior.getUser());
        ongoingState.playCard(pasoAtrasIngame.getId(), null, token);

        assertThat(warrior.getHand()).hasSize(2);
    }

    @Test
    void testBuyMarketCard() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);
        
        ranger.setGold(10);
        String playerToken = TokenUtils.generateJWTToken(ranger.getUser());
        turnService.setNextState(gameTester.getCurrentTurn());
        Integer FULL_MARKET = 5;

        assertThat(gameTester.getMarketCardsForSale()).hasSize(FULL_MARKET);

        MarketCard pocionCurativa = marketCardService.findMarketCardById(3).get();
        MarketCardIngame pocionCurativaIngame = marketCardIngameService.createFromMarketCard(pocionCurativa, gameTester);
        List<MarketCardIngame> market = gameTester.getMarketCardsForSale();
        market.get(0).setMarketCard(pocionCurativaIngame.getMarketCard());
        marketCardIngameService.buyMarketCard(market.get(0).getId(), playerToken);
        Integer FULL_MARKET_LESS_ONE = 4;
        Integer GOLD_LEFT = 2;

        assertThat(gameTester.getMarketCardsForSale()).hasSize(FULL_MARKET_LESS_ONE);
        assertThat(ranger.getGold()).isEqualTo(GOLD_LEFT);
        assertThat(ranger.getHand().get(ranger.getHand().size()-1).getAbilityCardTypeEnum()).isEqualTo(AbilityCardTypeEnum.POCION_CURATIVA);
    }

    @Test
    void testStartGame_Failure() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        Integer gameId = gameTester.getId();

        assertThrows(IllegalStateException.class, () -> ongoingState.startGame(gameId));
    }

    @Test
    void testFinishGame_Ranger_Wins_By_Glory() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        ranger.setGlory(10);
        ongoingState.finishGame(gameTester);

        assertThat(gameTester.getHasFinished()).isTrue();
        assertThat(gameTester.getStateType()).isEqualTo(GameStateType.FINISHED);
        assertThat(gameTester.getWinner()).isEqualTo(ranger);
    } 

    @Test
    void testFinishGame_Ranger_Wins_Rogue_Is_Wounded() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        rogue.setWounds(1);
        ongoingState.finishGame(gameTester);

        assertThat(gameTester.getHasFinished()).isTrue();
        assertThat(gameTester.getStateType()).isEqualTo(GameStateType.FINISHED);
        assertThat(gameTester.getWinner()).isEqualTo(ranger);
    }   
    
    @Test
    void testFinishGame_Ranger_Wins_Has_More_Money() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        ranger.setGold(3);
        ongoingState.finishGame(gameTester);

        assertThat(gameTester.getHasFinished()).isTrue();
        assertThat(gameTester.getStateType()).isEqualTo(GameStateType.FINISHED);
        assertThat(gameTester.getWinner()).isEqualTo(ranger);
    } 

    @Test
    void testFinishGame_Leader_Wins_By_Tie() {
        assertThat(actualState).isEqualTo(GameStateType.ONGOING);

        ongoingState.finishGame(gameTester);

        assertThat(gameTester.getHasFinished()).isTrue();
        assertThat(gameTester.getStateType()).isEqualTo(GameStateType.FINISHED);
        assertThat(gameTester.getWinner()).isEqualTo(gameTester.getLeader());
    }



}
