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
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.enemy.Enemy;
import org.springframework.ntfh.entity.enemy.EnemyService;
import org.springframework.ntfh.entity.enemy.EnemyType;
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
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnStateType;
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
public class WarriorCardTest {
    
    @Autowired
	private GameService gameService;

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

	protected Player warrior;

	protected Player rogue;

	protected EnemyIngame slingerIngame;

	protected EnemyIngame berserkerIngame;

	@BeforeEach
	public void init() {

		gameTester = new Game();
		gameTester.setName("test game");
		gameTester.setHasScenes(false);
		gameTester.setSpectatorsAllowed(false);
		gameTester.setMaxPlayers(2);
		gameTester.setStateType(GameStateType.LOBBY);
		gameTester = gameService.save(gameTester);

		User user1 = userService.findUser("user1");
		User user2 = userService.findUser("user2");

		gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
		gameTester = gameService.joinGame(gameTester, user2);

		warrior = gameTester.getPlayers().get(0);
		rogue = gameTester.getPlayers().get(1);

		Character warriorCharacter = characterService.findById(6);
		Character rogueCharacter = characterService.findById(4);

		warrior.setCharacter(warriorCharacter);
		rogue.setCharacter(rogueCharacter);

		gameService.startGame(gameTester.getId());
		Enemy SLINGER = enemyService.findEnemyById(9).get();
		slingerIngame = enemyIngameService.createFromEnemy(SLINGER, gameTester);
		Enemy BERSERKER = enemyService.findEnemyById(14).get();
		berserkerIngame = enemyIngameService.createFromEnemy(BERSERKER, gameTester);
	}

	@AfterEach
	public void teardown() {
		gameService.delete(gameTester);
	}
    
	@Test
	void testAtaqueBrutal(){
		AbilityCard ataqueBrutal = abilityCardService.findById(16);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(ataqueBrutal, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				berserkerIngame.getId(), tokenWarrior);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(3);
		assertThat(warrior.getDiscardPile().size()).isEqualTo(2);
	}

	@Test
	void testCargaConEscudo(){
		AbilityCard cargaConEscudo = abilityCardService.findById(18);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(cargaConEscudo, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				berserkerIngame.getId(), tokenWarrior);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
		assertThat(warrior.getGuard()).isEqualTo(2);
	}

	@Test
	void testDobleEspadazo(){
		AbilityCard dobleEspadazo = abilityCardService.findById(19);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(dobleEspadazo, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				berserkerIngame.getId(), tokenWarrior);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
		assertThat(warrior.getDiscardPile().size()).isEqualTo(2);
	}

	@Test
	void testEscudo(){

		Integer gameId = gameTester.getId();
		Turn currentTurn = gameService.getCurrentTurnByGameId(gameId);
		TurnStateType initialTurnState = currentTurn.getStateType();

		AbilityCard escudo = abilityCardService.findById(21);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(escudo, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				berserkerIngame.getId(), tokenWarrior);

        assertThat(berserkerIngame.getRestrained()).isTrue();
		assertThat(currentTurn.getStateType()).isNotEqualTo(initialTurnState);
	}

	@Test
	void testEspadazo(){

		// first espadazo in a turn

		List<EnemyIngame> enemiesFighting = List.of(slingerIngame, berserkerIngame);
		gameTester.setEnemiesFighting(enemiesFighting);
		AbilityCard espadazo = abilityCardService.findById(23);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(espadazo, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				berserkerIngame.getId(), tokenWarrior);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(5);
		assertThat(warrior.getHand().size()).isEqualTo(1);

		// second espadazo in a turn will not trigger the draw command

		hand.clear();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
		berserkerIngame.getId(), tokenWarrior);

		assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(4);
		assertThat(warrior.getHand().size()).isZero();
	}

	@Test
	void testPasoAtras(){
		AbilityCard pasoAtras = abilityCardService.findById(27);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(pasoAtras, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				null, tokenWarrior);

		assertThat(warrior.getHand().size()).isEqualTo(2);
	}

	@Test
	void testTodoONada(){

		//the card found deals 0 damage

		AbilityCard dobleEspadazo = abilityCardService.findById(19);
        AbilityCardIngame dobleEspadazoIngame =
        abilityCardIngameService.createFromAbilityCard(dobleEspadazo, warrior);
		AbilityCard pasoAtras = abilityCardService.findById(27);
        AbilityCardIngame pasoAtrasIngame =
        abilityCardIngameService.createFromAbilityCard(pasoAtras, warrior);

		
		AbilityCard todoONada = abilityCardService.findById(29);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(todoONada, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> abilityPile = new ArrayList<>();
		abilityPile.add(pasoAtrasIngame);
		abilityPile.add(dobleEspadazoIngame);
		warrior.setAbilityPile(abilityPile);
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				berserkerIngame.getId(), tokenWarrior);

        assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(5);
		assertThat(warrior.getDiscardPile().size()).isEqualTo(1); //the card that added 0 damage is still in the ability pile

		//the card found deals 2 damage

		List<AbilityCardIngame> discardPile = new ArrayList<>();
		warrior.setDiscardPile(discardPile);
		abilityPile.clear();
		abilityPile.add(dobleEspadazoIngame);
		abilityPile.add(pasoAtrasIngame);
		warrior.setAbilityPile(abilityPile);
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);

		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
		berserkerIngame.getId(), tokenWarrior);
		
		assertThat(berserkerIngame.getCurrentEndurance()).isEqualTo(2);
		assertThat(warrior.getDiscardPile().size()).isEqualTo(1); //the card that provided the bonus damage is still in the ability pile
	}

	@Test
	void vozDeAliento(){
		new DiscardCommand(2, rogue).execute();
		
		AbilityCard vozDeAliento = abilityCardService.findById(30);
        AbilityCardIngame abilityCardIngameWarrior =
        abilityCardIngameService.createFromAbilityCard(vozDeAliento, warrior);
        String tokenWarrior = TokenUtils.generateJWTToken(warrior.getUser());
		List<AbilityCardIngame> hand = new ArrayList<>();
		hand.add(abilityCardIngameWarrior);
		warrior.setHand(hand);
		abilityCardIngameService.playCard(abilityCardIngameWarrior.getId(),
				null, tokenWarrior);
		
		assertThat(warrior.getHand().size()).isEqualTo(1); //the card drawn
		assertThat(warrior.getGlory()).isEqualTo(1);
		assertThat(rogue.getDiscardPile().size()).isZero();
	}
}
