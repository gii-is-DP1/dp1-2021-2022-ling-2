package org.springframework.ntfh.commandtest;

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
import org.springframework.ntfh.command.AttackPhaseEndCommand;
import org.springframework.ntfh.command.ChangeEnemyCommand;
import org.springframework.ntfh.command.DealDamageCommand;
import org.springframework.ntfh.command.DiscardCommand;
import org.springframework.ntfh.command.DrawCommand;
import org.springframework.ntfh.command.ExileCommand;
import org.springframework.ntfh.command.GiveGloryCommand;
import org.springframework.ntfh.command.GiveGoldCommand;
import org.springframework.ntfh.command.GiveGuardCommand;
import org.springframework.ntfh.command.GiveWoundCommand;
import org.springframework.ntfh.command.GoldOnKillCommand;
import org.springframework.ntfh.command.HandToAbilityPileCommand;
import org.springframework.ntfh.command.HealCommand;
import org.springframework.ntfh.command.ReceiveDamageCommand;
import org.springframework.ntfh.command.RecoverCardCommand;
import org.springframework.ntfh.command.RecoverCommand;
import org.springframework.ntfh.command.RestrainCommand;
import org.springframework.ntfh.command.StealCoinCommand;
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
@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class CommandIngameTest {

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

    protected Player ranger;

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

        User user1 = userService.findByUsername("user1");
        User user2 = userService.findByUsername("user2");

        gameTester = gameService.joinGame(gameTester, user1); // first player -> leader
        gameTester = gameService.joinGame(gameTester, user2);

        ranger = gameTester.getPlayers().get(0);
        rogue = gameTester.getPlayers().get(1);

        Character rangerCharacter = characterService.findById(2);
        Character rogueCharacter = characterService.findById(4);

        ranger.setCharacter(rangerCharacter);
        rogue.setCharacter(rogueCharacter);

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
    void testAttackPhaseEndCommand() {

        // The phase of combat changes once used

        Integer gameId = gameTester.getId();
        Turn initialTurn = gameService.getCurrentTurnByGameId(gameId);
        TurnStateType initialTurnState = initialTurn.getStateType();
        new AttackPhaseEndCommand(gameService, ranger).execute();

        assertThat(initialTurn.getStateType()).isNotEqualTo(initialTurnState);
    }


    @Test
    void testChangeEnemyCommand() {

        // Replaces an enemy on the table with the one lowest on the enemy pile

        enemyIngameService.initializeFromGame(gameTester);
        List<EnemyIngame> initialEnemiesFighting = gameTester.getEnemiesFighting();
        EnemyIngame changedEnemy = initialEnemiesFighting.get(0);
        Integer numEnemiesOnTable = initialEnemiesFighting.size();

        new ChangeEnemyCommand(ranger, changedEnemy).execute();
        List<EnemyIngame> currentEnemiesFighting = gameTester.getEnemiesFighting();
        Integer currentNumEnemiesonTable = currentEnemiesFighting.size();
        EnemyType typeEnemyDrawn = currentEnemiesFighting.get(currentNumEnemiesonTable - 1).getEnemy().getEnemyType();
        List<EnemyType> warlords = List.of(EnemyType.SHRIEKKNIFER, EnemyType.GURDRUG, EnemyType.ROGHKILLER);

        assertThat(currentEnemiesFighting).isNotEmpty().doesNotContain(changedEnemy);
        assertThat(currentNumEnemiesonTable).isEqualTo(numEnemiesOnTable);
        assertThat(warlords).isNotEmpty().doesNotContain(typeEnemyDrawn);
    }

    @Test
    void testDealDamageCommand() {

        EnemyIngame shriekkniferIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(30).get(), gameTester);
        EnemyIngame gurdrukIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(28).get(), gameTester);

        List<EnemyIngame> listEnemiesFighting = new ArrayList<>();
        listEnemiesFighting.add(shriekkniferIngame);
        listEnemiesFighting.add(berserkerIngame);
        listEnemiesFighting.add(slingerIngame);
        gameTester.setEnemiesFighting(listEnemiesFighting);

        // Deal damage to a monster

        Integer initialEndurance = slingerIngame.getCurrentEndurance();
        new DealDamageCommand(1, ranger, slingerIngame).execute();

        assertThat(slingerIngame.getCurrentEndurance()).isEqualTo(initialEndurance - 1);

        // effects on kill of the enemy on the table

        Integer baseGlory = ranger.getGlory();
        Integer baseGold = ranger.getGold();
        Integer baseKills = ranger.getKills();
        Integer gloryAdded = berserkerIngame.getEnemy().getBaseGlory() + berserkerIngame.getEnemy().getExtraGlory();
        Integer goldAdded = berserkerIngame.getEnemy().getGold();
        new DealDamageCommand(10, ranger, berserkerIngame).execute();

        assertThat(ranger.getGlory()).isEqualTo(baseGlory + gloryAdded);
        assertThat(ranger.getGold()).isEqualTo(baseGold + goldAdded);
        assertThat(ranger.getKills()).isEqualTo(baseKills + 1);
        assertThat(gameTester.getEnemiesFighting()).doesNotContain(berserkerIngame);

        // if for any reason we dealt damage to an enemy who is already at 0 his endurance wouldnt change

        new DealDamageCommand(2, ranger, berserkerIngame).execute();

        assertThat(berserkerIngame.getCurrentEndurance()).isZero();

        // horde boss Shrieknifer is present on table and he applies his ability

        berserkerIngame.setCurrentEndurance(6);
        ranger.setGlory(0);

        new DealDamageCommand(1, ranger, shriekkniferIngame).execute();

        assertThat(ranger.getGlory()).isEqualTo(2);

        new DealDamageCommand(1, ranger, berserkerIngame).execute();

        assertThat(ranger.getGlory()).isEqualTo(3);

        // horde boss gurdruk is target of an attack and he applies his ability

        new DealDamageCommand(1, ranger, gurdrukIngame).execute();

        assertThat(ranger.getDiscardPile().size()).isEqualTo(1);


    }

    @Test
    void testDiscardCommand() {

        // Player discards a card

        Integer initialDiscardedCards = ranger.getDiscardPile().size();
        new DiscardCommand(1, ranger).execute();
        Integer discardedCards = ranger.getDiscardPile().size();

        assertThat(initialDiscardedCards).isZero();
        assertThat(discardedCards).isEqualTo(1);

        // Huge hit that empties the ability pile (should refill the ability pile, still take cards
        // away
        // from it, and
        // add a wound to the player)

        new DiscardCommand(17, ranger).execute();
        Integer abilityPileSize = ranger.getAbilityPile().size();
        Integer discardPileSize = ranger.getDiscardPile().size();

        assertThat(abilityPileSize).isNotZero();
        assertThat(discardPileSize).isNotZero();
        assertThat(ranger.getWounds()).isNotZero();
    }

    @Test
    void testDrawCommand() {

        // draw a card from the ability pile
        Integer initialHand = ranger.getHand().size();
        new DrawCommand(1, ranger).execute();
        Integer currentHand = ranger.getHand().size();

        assertThat(initialHand).isEqualTo(4);
        assertThat(currentHand).isEqualTo(5);

        // drawing more cards than there are currently on the ability pile (should add a wound and
        // then draw
        // the rest)

        new DiscardCommand(8, ranger).execute();
        new DrawCommand(8, ranger).execute();

        assertThat(ranger.getDiscardPile().size()).isZero();
        assertThat(ranger.getWounds()).isNotZero();
    }


    @Test
    void testExileCommand() {

        // exile a card

        AbilityCard pocionCurativa = abilityCardService.findById(62);
        AbilityCardIngame pocionCurativaIngame = abilityCardIngameService.createFromAbilityCard(pocionCurativa, ranger);
        List<AbilityCardIngame> currentHand = ranger.getHand();
        currentHand.add(pocionCurativaIngame);
        ranger.setHand(currentHand);

        assertThat(currentHand.size()).isEqualTo(5);
        assertThat(ranger.getDiscardPile().size()).isZero();

        new ExileCommand(ranger, AbilityCardTypeEnum.POCION_CURATIVA).execute();

        assertThat(currentHand.size()).isEqualTo(4);
        assertThat(ranger.getDiscardPile().size()).isZero();
    }

    @Test
    void testGiveGlory() {

        // give glory to a player

        Integer initialGlory = ranger.getGlory();
        Integer amount = 5;
        new GiveGloryCommand(amount, ranger).execute();
        Integer currentGlory = ranger.getGlory();

        assertThat(currentGlory).isEqualTo(initialGlory + amount);

        // substract glory

        Integer amountSubstracted = -2;
        new GiveGloryCommand(amountSubstracted, ranger).execute();
        Integer gloryAfterSubstract = ranger.getGlory();
        Integer gloryAfterSubstractGoal = initialGlory + amount + amountSubstracted;

        assertThat(gloryAfterSubstract).isEqualTo(gloryAfterSubstractGoal);

        // substract more than the player has

        new GiveGloryCommand(-100, ranger).execute();

        assertThat(ranger.getGlory()).isZero();
    }

    @Test
    void testGiveGold() {
        // give gold to a player

        Integer initialGold = ranger.getGold();
        Integer amount = 10;
        new GiveGoldCommand(amount, ranger).execute();
        Integer currentGold = ranger.getGold();

        assertThat(currentGold).isEqualTo(initialGold + amount);

        // substract gold

        Integer amountSubstracted = -5;
        new GiveGoldCommand(amountSubstracted, ranger).execute();
        Integer goldAfterSubstract = ranger.getGold();
        Integer goldAfterSubstractGoal = initialGold + amount + amountSubstracted;

        assertThat(goldAfterSubstract).isEqualTo(goldAfterSubstractGoal);

        // substract more than the player has

        new GiveGoldCommand(-100, ranger).execute();

        assertThat(ranger.getGold()).isZero();
    }

    @Test
    void testGiveGuard() {
        // add guard

        Integer guardApplied = 1;
        new GiveGuardCommand(guardApplied, ranger).execute();

        assertThat(ranger.getGuard()).isEqualTo(guardApplied);

        // add a greater guard on top of guard (should remove the lower of the 2 and keep the
        // highest)

        Integer anotherGuard = 2;
        new GiveGuardCommand(anotherGuard, ranger).execute();

        assertThat(ranger.getGuard()).isEqualTo(anotherGuard);

        // add a lesser guard (will apply negative as well on this case to do them both at once)

        Integer negativeGuard = -1;
        new GiveGuardCommand(negativeGuard, ranger);

        assertThat(ranger.getGuard()).isEqualTo(anotherGuard);
    }

    @Test
    void testGiveWound() {

        // wounding a player

        new GiveWoundCommand(rogue).execute();

        assertThat(rogue.getWounds()).isEqualTo(1);
        assertThat(rogue.isDead()).isFalse();

        // killing a player

        new GiveWoundCommand(rogue).execute();

        assertThat(rogue.getWounds()).isEqualTo(2);
        assertThat(rogue.isDead()).isTrue();
    }

    @Test
    void testGoldOnKill() {

        // gives gold on the kill of an enemy

        slingerIngame.setCurrentEndurance(0);
        new GoldOnKillCommand(1, slingerIngame, ranger).execute();

        assertThat(ranger.getGold()).isEqualTo(1);

        // it will not give the gold if the enemy is still alive

        slingerIngame.setCurrentEndurance(1);
        new GoldOnKillCommand(100, slingerIngame, ranger);

        assertThat(ranger.getGold()).isEqualTo(1);
    }

    @Test
    void testHandToAbilityPile() {

        // sends the card from the hand to the ability pile

        AbilityCard companeroLobo = abilityCardService.findById(1);
        AbilityCardIngame abilityCardIngame = abilityCardIngameService.createFromAbilityCard(companeroLobo, ranger);
        ranger.getHand().add(abilityCardIngame);

        assertThat(ranger.getAbilityPile().size()).isEqualTo(11);
        assertThat(ranger.getHand().size()).isEqualTo(5);

        new HandToAbilityPileCommand(ranger, companeroLobo.getAbilityCardTypeEnum()).execute();

        assertThat(ranger.getAbilityPile().size()).isEqualTo(12);
        assertThat(ranger.getHand().size()).isEqualTo(4);

        // check it doesnt return all cards of the same enum type

        AbilityCard dagaElfica1 = abilityCardService.findById(61);
        AbilityCardIngame dagaElfica1Ingame = abilityCardIngameService.createFromAbilityCard(dagaElfica1, rogue);
        rogue.getHand().add(dagaElfica1Ingame);
        AbilityCard dagaElfica2 = abilityCardService.findById(61);
        AbilityCardIngame dagaElfica2Ingame = abilityCardIngameService.createFromAbilityCard(dagaElfica2, rogue);
        rogue.getHand().add(dagaElfica2Ingame);

        assertThat(rogue.getAbilityPile().size()).isEqualTo(11);
        assertThat(rogue.getHand().size()).isEqualTo(6);

        new HandToAbilityPileCommand(rogue, dagaElfica1.getAbilityCardTypeEnum()).execute();

        assertThat(rogue.getAbilityPile().size()).isEqualTo(12);
        assertThat(rogue.getHand().size()).isEqualTo(5);


    }

    @Test
    void testHeal() {

        // heal a player

        ranger.setWounds(2);
        new HealCommand(ranger).execute();

        assertThat(ranger.getWounds()).isEqualTo(1);

        // heal a player beyond their maximum hp

        new HealCommand(ranger).execute();
        new HealCommand(ranger).execute();

        assertThat(ranger.getWounds()).isZero();
    }

    @Test
    void testReceiveDamage() {

        // recieve damage, not enough to wound

        new ReceiveDamageCommand(slingerIngame, rogue).execute();

        assertThat(rogue.getDiscardPile().size()).isEqualTo(2);
        assertThat(rogue.getWounds()).isZero();

        // recieve damage beyond their current ability pile, ads wound

        // instantiate a hard hitting enemy

        new ReceiveDamageCommand(berserkerIngame, rogue).execute();
        new ReceiveDamageCommand(berserkerIngame, rogue).execute();
        new ReceiveDamageCommand(berserkerIngame, rogue).execute();

        assertThat(rogue.getAbilityPile().size()).isNotZero();
        assertThat(rogue.getDiscardPile().size()).isNotZero();
        assertThat(rogue.getWounds()).isEqualTo(1);

        // recieve damage and this damage kills

        new ReceiveDamageCommand(slingerIngame, rogue).execute();

        assertThat(rogue.getWounds()).isEqualTo(2);
        assertThat(rogue.isDead()).isTrue();

        // receive damage and the roghkiller warlord ability is active

        EnemyIngame roghkillerIngame = enemyIngameService.createFromEnemy(enemyService.findEnemyById(29).get(), gameTester);
        List<EnemyIngame> listEnemiesFighting = gameTester.getEnemiesFighting();
        listEnemiesFighting.add(roghkillerIngame);
        gameTester.setEnemiesFighting(listEnemiesFighting);

        rogue.setWounds(0);

        new ReceiveDamageCommand(slingerIngame, rogue).execute();

        assertThat(rogue.getDiscardPile().size()).isEqualTo(3); //the slinger dealt 1 more damage as expected
        
    }

    @Test
    void testRecoverCardCommand() {

        // search for a card that is in the discard pile and recover it

        AbilityCard disparoRapido = abilityCardService.findById(4);
        AbilityCardIngame abilityCardIngame = abilityCardIngameService.createFromAbilityCard(disparoRapido, ranger);
        String token = TokenUtils.generateJWTToken(ranger.getUser());
        List<AbilityCardIngame> hand = new ArrayList<>();
        hand.add(abilityCardIngame);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngame.getId(), gameTester.getEnemiesFighting().get(0).getId(),
                token);

        assertThat(ranger.getDiscardPile().size()).isEqualTo(1);

        new RecoverCardCommand(ranger, AbilityCardTypeEnum.DISPARO_RAPIDO).execute();

        assertThat(ranger.getDiscardPile().size()).isZero();

        // search for a card that isnt in the discard pile, after not finding the command doesnt
        // make any
        // further
        // actions

        hand.add(abilityCardIngame);
        ranger.setHand(hand);
        abilityCardIngameService.playCard(abilityCardIngame.getId(), gameTester.getEnemiesFighting().get(0).getId(),
                token);

        assertThat(ranger.getDiscardPile().size()).isEqualTo(1);

        new RecoverCardCommand(ranger, AbilityCardTypeEnum.COMPANERO_LOBO).execute();

        assertThat(ranger.getDiscardPile().size()).isEqualTo(1);
    }

    @Test
    void testRecoverCommand() {

        // recover one card from the discard pile to the draw pile

        new DiscardCommand(1, ranger).execute();
        new RecoverCommand(ranger).execute();

        assertThat(ranger.getAbilityPile().size()).isEqualTo(11);
        assertThat(ranger.getDiscardPile().size()).isZero();

        // try to recover one card from an empty discard pile to the draw pile

        new RecoverCommand(ranger).execute();

        assertThat(ranger.getAbilityPile().size()).isEqualTo(11);
        assertThat(ranger.getDiscardPile().size()).isZero();
    }


    @Test
    void testRestrainCommand() {

        // check if the enemy is restrained after executing the command

        assertThat(slingerIngame.getRestrained()).isFalse();

        new RestrainCommand(slingerIngame).execute();

        assertThat(slingerIngame.getRestrained()).isTrue();

        // Check if we restrain an already restrained enemy it will still be restrained

        new RestrainCommand(slingerIngame).execute();

        assertThat(slingerIngame.getRestrained()).isTrue();
    }

    @Test
    void testStealCoinCommand() {

        // the hero/heroes affected have gold to be stolen

        ranger.setGold(10);

        assertThat(rogue.getGold()).isZero();
        assertThat(ranger.getGold()).isEqualTo(10);

        new StealCoinCommand(rogue, ranger).execute();

        assertThat(rogue.getGold()).isEqualTo(1);
        assertThat(ranger.getGold()).isEqualTo(9);

        // the hero/heroes affected dont have gold to be stolen

        ranger.setGold(0);
        rogue.setGold(1);

        new StealCoinCommand(rogue, ranger).execute();

        assertThat(rogue.getGold()).isEqualTo(1);
        assertThat(ranger.getGold()).isZero();
    }

}
