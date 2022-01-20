package org.springframework.ntfh.cardtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.GiveWoundCommand;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.game.GameStateType;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCard;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
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
@DataJpaTest(
		includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
public class MarketCardTest {
    
    @Autowired
	private GameService gameService;

    @Autowired
    private TurnService turnService;

	@Autowired
	private UserService userService;

	@Autowired
	private CharacterService characterService;

	@Autowired
	private AbilityCardIngameService abilityCardIngameService;

	@Autowired
	private EnemyService enemyService;

	@Autowired
	private EnemyIngameService enemyIngameService;

	@Autowired
	private AbilityCardService abilityCardService;

	protected Game gameTester;

	protected Player ranger;

	protected Player rogue;

    protected Player wizard;

	protected Player warrior;

	protected EnemyIngame slingerIngame;

    protected EnemyIngame berserkerIngame;

	@BeforeEach
	public void init() {

		gameTester = new Game();
		gameTester.setName("test game");
		gameTester.setHasScenes(false);
		gameTester.setSpectatorsAllowed(false);
		gameTester.setMaxPlayers(4);
		gameTester.setStateType(GameStateType.LOBBY);
		gameTester = gameService.save(gameTester);

		User user1 = userService.findUser("user1");
		User user2 = userService.findUser("user2");
        User user3 = userService.findUser("user3");
        User user4 = userService.findUser("user4");

		gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
		gameTester = gameService.joinGame(gameTester, user2);
        gameTester = gameService.joinGame(gameTester, user3);
        gameTester = gameService.joinGame(gameTester, user4);

		ranger = gameTester.getPlayers().get(0);
		rogue = gameTester.getPlayers().get(1);
        warrior = gameTester.getPlayers().get(2);
        wizard = gameTester.getPlayers().get(3);

		Character rangerCharacter = characterService.findById(2);
		Character rogueCharacter = characterService.findById(4);
		Character warriorCharacter = characterService.findById(6);
        Character wizardCharacter = characterService.findById(8);

		ranger.setCharacter(rangerCharacter);
		rogue.setCharacter(rogueCharacter);
        wizard.setCharacter(wizardCharacter);
        warrior.setCharacter(warriorCharacter);

		gameService.startGame(gameTester.getId());
		Enemy SLINGER = enemyService.findEnemyById(12).get();
		slingerIngame = enemyIngameService.createFromEnemy(SLINGER, gameTester);
        Enemy BERSERKER = enemyService.findEnemyById(15).get();
        berserkerIngame = enemyIngameService.createFromEnemy(BERSERKER, gameTester);
	}

	@AfterEach
	public void teardown() {
		gameService.delete(gameTester);
	}

    @Test
    void testAlabardaOrca(){

        //card deals full damage

        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of rogue
        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of warrior

        AbilityCard alabardaOrca = abilityCardService.findById(68);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(alabardaOrca, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				berserkerIngame.getId(), tokenWarrior);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(2);

        //card deals 3 damage due to a lack of full proficiency

        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of wizard
        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of ranger

        berserkerIngame.setCurrentEndurance(6); //heal the enemy to allow for a correct calculation
        AbilityCardIngame abilityCardIngameRanger =
        abilityCardIngameService.createFromAbilityCard(alabardaOrca, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        hand.clear();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
                berserkerIngame.getId(), tokenRanger);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(3);
    }

    @Test
    void testArcoCompuesto(){

        //card deals full damage
        
        AbilityCard arcoCompuesto = abilityCardService.findById(69);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(arcoCompuesto, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
                berserkerIngame.getId(), tokenRanger);
        
        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(2);
        
        //card deals 3 damage
        
        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of rogue
        
        berserkerIngame.setCurrentEndurance(6); //heal the enemy to allow for a correct calculation
        AbilityCardIngame abilityCardIngameRogue =
        abilityCardIngameService.createFromAbilityCard(arcoCompuesto, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        hand.clear();
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(),
                berserkerIngame.getId(), tokenRogue);
        
        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(3);
    }

    @Test
    void testArmaduraDePlacas(){

        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of rogue
        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of warrior

        new DiscardCommand(4, warrior).execute();

        assertThat(warrior.getDiscardPile().size()).isEqualTo(4);

        AbilityCard armaduraDePlacas = abilityCardService.findById(67);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(armaduraDePlacas, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				null, tokenWarrior);
        
        assertThat(warrior.getDiscardPile().size()).isEqualTo(1); //recovered the 4 cards but then discards, thats why this is 1
    }

    @Test
    void testCapaElfica(){

        AbilityCard capaElfica = abilityCardService.findById(66);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(capaElfica, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
                berserkerIngame.getId(), tokenRanger);
        assertThat(berserkerIngame.getRestrained()).isTrue();
    }

    @Test
    void testDagaElfica(){

        AbilityCard dagaElfica = abilityCardService.findById(61);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(dagaElfica, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
                berserkerIngame.getId(), tokenRanger);
        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(ranger.getDiscardPile().size()).isEqualTo(1); //the card should be discarded

        // case where proficiency is applied

        turnService.setNextState(gameTester.getCurrentTurn());
        turnService.setNextState(gameTester.getCurrentTurn()); //turn of rogue
        
        berserkerIngame.setCurrentEndurance(6); //heal the enemy to allow for a correct calculation
        AbilityCardIngame abilityCardIngameRogue =
        abilityCardIngameService.createFromAbilityCard(dagaElfica, rogue);
        String tokenRogue = TokenUtils.generateJWTToken(rogue.getUser());
        hand.add(abilityCardIngameRogue);
        rogue.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRogue.getId(),
                berserkerIngame.getId(), tokenRogue);
        
        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
        assertThat(rogue.getDiscardPile().size()).isZero(); //the card should return to the ability pile since he has proficiency
    }

    @Test
    void testElixirDeConcentracion(){

        AbilityCard elixirDeConcentracion = abilityCardService.findById(65);
        AbilityCardIngame abilityCardIngameRanger =
        abilityCardIngameService.createFromAbilityCard(elixirDeConcentracion, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameRanger);
		ranger.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
				null, tokenRanger);
        
        assertThat(ranger.getHand().size()).isEqualTo(3); // added 3 cards to the hand

    }

    @Test
    void testPiedraDeAmolar(){

        List<EnemyIngame> listEnemiesFighting = List.of(berserkerIngame);
        gameTester.setEnemiesFighting(listEnemiesFighting);
        AbilityCard piedraDeAmolar = abilityCardService.findById(63);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(piedraDeAmolar, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
                null, tokenRanger);
        
        assertThat(berserkerIngame.getPlayedCardsOnMeInTurn()).isNotEmpty().contains(AbilityCardTypeEnum.PIEDRA_DE_AMOLAR);
    }

    @Test
    void testPocionCurativa(){

        new GiveWoundCommand(ranger).execute();

        assertThat(ranger.getWounds()).isEqualTo(1);

        AbilityCard pocionCurativa = abilityCardService.findById(62);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(pocionCurativa, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
                null, tokenRanger);
        
        assertThat(ranger.getWounds()).isZero();
        assertThat(ranger.getDiscardPile().size()).isZero();
    }

    @Test
    void testVialDeConjuracion(){

        AbilityCard vialDeConjuracion = abilityCardService.findById(64);
        AbilityCardIngame abilityCardIngameRanger =
                abilityCardIngameService.createFromAbilityCard(vialDeConjuracion, ranger);
        String tokenRanger = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngameRanger);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngameRanger.getId(),
                null, tokenRanger);
        
        assertThat(ranger.getDiscardPile().size()).isEqualTo(1); //recover 1 card
        assertThat(ranger.getHand().size()).isEqualTo(1); //draw 1 card
    }
}
