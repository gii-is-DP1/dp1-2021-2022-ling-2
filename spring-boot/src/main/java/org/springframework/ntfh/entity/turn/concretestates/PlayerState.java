package org.springframework.ntfh.entity.turn.concretestates;

import java.lang.reflect.Method;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnService;
import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.ntfh.util.State;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@State
public class PlayerState implements TurnState {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private EnemyIngameService enemyIngameService;

    @Autowired
    private TurnService turnService;

    @Autowired
    private GameService gameService;


    @Override
    public TurnStateType getNextState() {
        return TurnStateType.MARKET_STATE;
    }

    @Override
    public void postState(Game game) {
        // PlayerState has no post state (it doesn't need to perforn any action before
        // the state changes to the market state)
    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {

        // TODO throw exception if the one sending the request is not the card owner

        AbilityCardIngame abilityCardIngame = abilityCardIngameService.findById(abilityCardIngameId);
        AbilityCardTypeEnum abilityCardTypeEnum = abilityCardIngame.getAbilityCard().getAbilityCardTypeEnum();
        Player playerFrom = abilityCardIngame.getPlayer();
        CharacterTypeEnum characterType = abilityCardIngame.getAbilityCard().getCharacterTypeEnum();

        // If the card does not belong to any type of character, it is a market card
        String characterTypeName = (characterType == null) ? "market" : characterType.toString().toLowerCase();

        Turn currentTurn = playerFrom.getGame().getCurrentTurn();
        if (!currentTurn.getPlayer().getId().equals(playerFrom.getId())) {
            throw new IllegalArgumentException("It's not your turn");
        }

        if (!playerFrom.getHand().contains(abilityCardIngame)) {
            throw new IllegalArgumentException("You don't have that card in your hand");
        }
        // Convert the enum to the appropiate PascalCase class name (DAGA_ELFICA ->
        // DagaElfica)
        String className = CaseUtils.toCamelCase(abilityCardTypeEnum.toString(), true, new char[] {'_'});
        String completeClassName =
                String.format("org.springframework.ntfh.cardlogic.abilitycard.%s.%s", characterTypeName, className);

        try {
            // Get the class from its name
            Class<?> clazz = Class.forName(completeClassName);
            // Instantiate an object of the class
            Object cardCommand = clazz.getDeclaredConstructor().newInstance();
            // Autowire the new object's dependencies (services used inside)
            AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
            factory.autowireBean(cardCommand);
            factory.initializeBean(cardCommand, className);
            if (enemyId == null) {
                // Handle self playable card (does not target a specific enemy)
                // Get a reference to the method "execute", that receives 2 parameters
                Method method = clazz.getDeclaredMethod("execute", Player.class);
                // Execute the method with the parameters
                method.invoke(cardCommand, playerFrom);
            } else {
                // Handle card that targets an enemy
                EnemyIngame targetedEnemy = enemyIngameService.findById(enemyId);
                Method method = clazz.getDeclaredMethod("execute", Player.class, EnemyIngame.class);
                method.invoke(cardCommand, playerFrom, targetedEnemy);
            }
        } catch (Exception e) {
            log.error("Error playing card ", e);
            throw new IllegalArgumentException(className + " cannot be played now");
        }

        // Make sure to move the card to the discard pile
        Player player = abilityCardIngame.getPlayer();
        if (player.getHand().contains(abilityCardIngame)) {
            player.getHand().remove(abilityCardIngame);
            player.getDiscardPile().add(abilityCardIngame);
        }

        // End the game if playing this card has killed the last enemy
        Game game = player.getGame();
        if (!game.hasEnemiesLeft()) {
            gameService.finishGame(game);
            return;
        }

        // End turn if plying this card has killed you
        if (Boolean.TRUE.equals(player.isDead())) {
            turnService.setNextState(currentTurn);
            turnService.setNextState(currentTurn);
        }
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        throw new IllegalStateException("You can't buy cards now");
    }

}
