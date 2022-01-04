package org.springframework.ntfh.entity.turn.concretestates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngame;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.AbilityCardTypeEnum;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngame;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.turn.TurnState;
import org.springframework.ntfh.entity.turn.TurnStateType;
import org.springframework.stereotype.Component;

@Component
public class PlayerState implements TurnState {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    @Autowired
    private EnemyIngameService enemyIngameService;

    @Override
    public TurnStateType getNextState() {
        return TurnStateType.MARKET_STATE;
    }

    @Override
    public void preState(Game game) {
        // TODO auto-generated method stub
    }

    @Override
    public void postState(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playCard(Integer abilityCardIngameId, Integer enemyId, String token) {

        // TODO throw exception if the one sending the request is not the card owner

        AbilityCardIngame abilityCardIngame = abilityCardIngameService.findById(abilityCardIngameId);
        AbilityCardTypeEnum abilityCardTypeEnum = abilityCardIngame.getAbilityCard().getAbilityCardTypeEnum();
        Player playerFrom = abilityCardIngame.getPlayer();
        String characterType = abilityCardIngame.getAbilityCard().getCharacterTypeEnum().toString().toLowerCase();

        Turn currentTurn = playerFrom.getGame().getCurrentTurn();
        if (!currentTurn.getPlayer().getId().equals(playerFrom.getId())) {
            throw new IllegalArgumentException("It's not your turn");
        }

        if (!playerFrom.getHand().contains(abilityCardIngame)) {
            throw new IllegalArgumentException("You don't have that card in your hand");
        }
        // Convert the enum to the appropiate PascalCase class name (DAGA_ELFICA ->
        // DagaElfica)
        String className = CaseUtils.toCamelCase(abilityCardTypeEnum.toString(), true,
                new char[] { '_' });
        String completeClassName = String.format("org.springframework.ntfh.cardlogic.abilitycard.%s.%s",
                characterType,
                className);

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
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("Ability card type " + className +
                    " is not implemented");
        }

        // Make sure to move the card to the discard pile
        Player player = abilityCardIngame.getPlayer();
        player.getHand().remove(abilityCardIngame);
        player.getDiscardPile().add(abilityCardIngame);

        // Check if the card is exiliable and if so, remove it from the discard pile too
        // TODO handled already? does it work?
    }

    @Override
    public void buyMarketCard(Integer marketCardIngameId, String token) {
        throw new IllegalStateException("You can't buy cards now");
    }

}
