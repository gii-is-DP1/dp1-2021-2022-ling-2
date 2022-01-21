package org.springframework.ntfh.turn.stateTests;

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
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
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
class MarketStateTest {

    @Autowired
    private TurnService turnService;

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

    protected User user1, user2, user3;

    protected Turn turnTester;

    protected MarketState marketState;

    protected TurnStateType actualState;

    @BeforeEach
    void init() {
        gameTester = new Game();
        gameTester.setName("test game");
        gameTester.setHasScenes(false);
        gameTester.setSpectatorsAllowed(false);
        gameTester.setMaxPlayers(2);
        gameTester.setStateType(GameStateType.LOBBY);
        gameTester = gameService.save(gameTester);

        user1 = userService.findByUsername("user1");
        user2 = userService.findByUsername("user2");
        user3 = userService.findByUsername("user3"); // User used for joining users test

        gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
        gameTester = gameService.joinGame(gameTester, user2);

        ranger = gameTester.getPlayers().get(0);
        rogue = gameTester.getPlayers().get(1);

        Character rangerCharacter = characterService.findById(2);
        Character rogueCharacter = characterService.findById(4);

        ranger.setCharacter(rangerCharacter);
        rogue.setCharacter(rogueCharacter);

        turnService.initializeFromGame(gameTester);

        turnTester = turnService.findturnById(gameTester.getCurrentTurn().getId()).get();

        turnService.setNextState(turnTester);

        marketState = (MarketState) turnService.getState(turnTester);

        actualState = turnTester.getStateType();
    }

    @AfterEach
    void teardown() {
        try {
            turnService.delete(turnTester.getId());
        } catch (Exception exception) {
        }
    }

    @Test
    void testGetNextState() {
        assertThat(actualState).isEqualTo(TurnStateType.MARKET_STATE);
        assertThat(marketState.getNextState()).isEqualTo(TurnStateType.PLAYER_STATE);
    }

    @Test
    void testPostState() {
        assertThat(actualState).isEqualTo(TurnStateType.MARKET_STATE);

        marketState.postState(gameTester);

        assertThat(ranger.getHand().size()).isNotZero();
    }

    @Test
    void testPlayCard_failure() {
        assertThat(actualState).isEqualTo(TurnStateType.MARKET_STATE);

        Player warrior = ranger;
        warrior.setCharacter(characterService.findById(5));
        AbilityCard pasoAtras = abilityCardService.findById(27);
        AbilityCardIngame pasoAtrasIngame = abilityCardIngameService.createFromAbilityCard(pasoAtras, warrior);
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(pasoAtrasIngame);
        warrior.setHand(hand);
        String token = TokenUtils.generateJWTToken(warrior.getUser());
        Integer abilityCardIngame = pasoAtrasIngame.getId();

        assertThrows(IllegalStateException.class, () -> marketState.playCard(abilityCardIngame, null, token));
    }

    @Test
    void testBuyMarketCard_Success() {
        assertThat(actualState).isEqualTo(TurnStateType.MARKET_STATE);

        ranger.setGold(10);
        String playerToken = TokenUtils.generateJWTToken(ranger.getUser());
        turnService.setNextState(gameTester.getCurrentTurn());
        Integer FULL_MARKET = 5;

        assertThat(gameTester.getMarketCardsForSale()).hasSize(FULL_MARKET);

        MarketCard pocionCurativa = marketCardService.findMarketCardById(3).get();
        MarketCardIngame pocionCurativaIngame =
                marketCardIngameService.createFromMarketCard(pocionCurativa, gameTester);
        List<MarketCardIngame> market = gameTester.getMarketCardsForSale();
        market.get(0).setMarketCard(pocionCurativaIngame.getMarketCard());
        Integer marketCardIngameId = market.get(0).getId();
        marketState.buyMarketCard(marketCardIngameId, playerToken);
        Integer FULL_MARKET_LESS_ONE = 4;
        Integer GOLD_LEFT = 2;

        assertThat(gameTester.getMarketCardsForSale()).hasSize(FULL_MARKET_LESS_ONE);
        assertThat(ranger.getGold()).isEqualTo(GOLD_LEFT);
        assertThat(ranger.getHand().get(ranger.getHand().size() - 1).getAbilityCardTypeEnum())
                .isEqualTo(AbilityCardTypeEnum.POCION_CURATIVA);
    }

    @Test
    void testBuyMarketCard_Failure_Not_Avaible_Card() {
        assertThat(actualState).isEqualTo(TurnStateType.MARKET_STATE);

        rogue.setGold(10);
        String playerToken = TokenUtils.generateJWTToken(rogue.getUser());
        turnService.setNextState(gameTester.getCurrentTurn());
        Integer FULL_MARKET = 5;

        assertThat(gameTester.getMarketCardsForSale()).hasSize(FULL_MARKET);

        MarketCard armaduraPlacas = marketCardService.findMarketCardById(12).get();
        MarketCardIngame armaduraPlacasIngame =
                marketCardIngameService.createFromMarketCard(armaduraPlacas, gameTester);
        List<MarketCardIngame> market = gameTester.getMarketCardsForSale();
        market.get(0).setMarketCard(armaduraPlacasIngame.getMarketCard());
        Integer marketCardIngameId = market.get(0).getId();

        assertThrows(IllegalArgumentException.class, () -> marketState.buyMarketCard(marketCardIngameId, playerToken));
    }

    @Test
    void testBuyMarketCard_Failure_Not_Enough_Gold() {
        assertThat(actualState).isEqualTo(TurnStateType.MARKET_STATE);

        rogue.setGold(7);
        String playerToken = TokenUtils.generateJWTToken(rogue.getUser());
        turnService.setNextState(gameTester.getCurrentTurn());
        Integer FULL_MARKET = 5;

        assertThat(gameTester.getMarketCardsForSale()).hasSize(FULL_MARKET);

        MarketCard pocionCurativa = marketCardService.findMarketCardById(3).get();
        MarketCardIngame pocionCurativaIngame =
                marketCardIngameService.createFromMarketCard(pocionCurativa, gameTester);
        List<MarketCardIngame> market = gameTester.getMarketCardsForSale();
        market.get(0).setMarketCard(pocionCurativaIngame.getMarketCard());
        Integer marketCardIngameId = market.get(0).getId();

        assertThrows(IllegalArgumentException.class, () -> marketState.buyMarketCard(marketCardIngameId, playerToken));
    }

}
