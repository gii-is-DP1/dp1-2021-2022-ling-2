package org.springframework.ntfh.entity.turn;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.enemy.EnemyModifierType;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.scene.Scene;
import org.springframework.ntfh.entity.scene.SceneService;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.stereotype.Service;

/**
 * @author andrsdt
 */
@Service
public class TurnService {

    @Autowired
    private TurnRepository turnRepository;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private EnemyIngameService enemyIngameService;

    @Autowired
    private MarketCardIngameService marketCardIngameService;

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;

    /************ STATES ************/

    @Autowired
    private MarketState marketState;

    @Autowired
    private PlayerState playerState;

    /*******************************/

    @Transactional
    public Integer turnCount() {
        return (int) turnRepository.count();
    }

    @Transactional
    public Iterable<Turn> findAll() {
        return turnRepository.findAll();
    }

    @Transactional
    public Optional<Turn> findturnById(Integer id) {
        return turnRepository.findById(id);
    }

    @Transactional
    public void save(Turn turn) {
        turnRepository.save(turn);
    }

    public void delete(int turnId) {
        turnRepository.deleteById(turnId);
    }

    /**
     * Create the initial turn for a game.
     * 
     * @author andrsdt
     * @param game that the turn will be created for
     */
    @Transactional
    public void initializeFromGame(Game game) {
        Turn turn = new Turn();
        turn.setPlayer(game.getLeader());

        if (game.getHasScenes()) {
            // Get a random scene and set it as the current scene
            Scene randomScene = sceneService
                    .findSceneById(new Random().nextInt(sceneService.count()) + 1).orElse(null); // DB indexes start in
                                                                                                 // 1
            turn.setCurrentScene(randomScene);
        }

        turn.setGame(game);

        // Initial state is the state where the player attacks
        turn.setStateType(TurnStateType.PLAYER_STATE);

        enemyIngameService.initializeFromGame(game);
        marketCardIngameService.initializeFromGame(game);
        abilityCardIngameService.initializeFromGame(game);
        turnRepository.save(turn);

        // Set a foreign key to the current turn in the game
        game.getTurns().add(turn);
    }

    public TurnState getState(Turn turn) {
        TurnStateType stateType = turn.getStateType();
        if (stateType == TurnStateType.PLAYER_STATE) {
            return playerState;
        } else if (stateType == TurnStateType.MARKET_STATE) {
            return marketState;
        } else {
            return null;
        }
    }

    public void setNextState(Turn turn) {
        TurnState state = getState(turn);
        state.postState(turn.getGame()); // Execute the post-state method of the current state before changing it
        TurnStateType nextState = state.getNextState();
        turn.setStateType(nextState);
    }

    /**
     * Given a game, create the next turn
     * 
     * @author andrsdt
     * @param game that the turn will be created for
     */
    @Transactional
    public void createNextTurn(Game game) {
        Turn currentTurn = game.getCurrentTurn();
        Turn nextTurn = new Turn();
        // The next player will be

        if (game.getHasScenes()) {
            // Get a random scene and set it as the current scene
            Scene randomScene = sceneService
                    .findSceneById(new Random().nextInt(sceneService.count()) + 1).orElse(null);
            nextTurn.setCurrentScene(randomScene);
        }

        game.getEnemiesFighting().forEach(e -> {
            e.getPlayedCardsOnMeInTurn().clear();
            e.setRestrained(false);
            if(e.getEnemy().getEnemyModifierType() != null && e.getEnemy().getEnemyModifierType().equals(EnemyModifierType.HEALING_CAPABILITIES)){
                e.setCurrentEndurance(e.getEnemy().getEndurance());
            }
        });

        // Get the next player. Following the previously set turnOrder, the next player
        // will be the one after the current player, considering they are alive. In case
        // there is no next player, the next player will be the first player (circular
        // list)
        // ! There will probably be a bug if currentTurn.getPlayer() dies since he/she
        // won't be in the list anymore and indexOf will return -1
        List<Player> alivePlayers = game.getAlivePlayersInTurnOrder();
        if (alivePlayers.isEmpty()) {
            // If there are no alive players, the game is over
            // TODO make the game finish early if everybody dies. Right now this returns an
            // IndexOutOfBoundsException because of the following line "alivePlayers.get(0)"
            return;
        }
        Player nextPlayer = alivePlayers.indexOf(currentTurn.getPlayer()) + 1 == alivePlayers.size()
                ? alivePlayers.get(0)
                : alivePlayers.get(alivePlayers.indexOf(currentTurn.getPlayer()) + 1);

        nextTurn.setPlayer(nextPlayer);
        nextTurn.setGame(game);

        // Initial state is the state where the player attacks
        nextTurn.setStateType(TurnStateType.PLAYER_STATE);

        // Add enemies to the table if some died in the previous turn
        enemyIngameService.refillTableWithEnemies(game);
        // Add market cards to the table if some were purchased in the previous turn
        marketCardIngameService.refillMarketWithCards(game);
        // Refill the current turn player's hand if it has less than 4 cards
        abilityCardIngameService.refillHandWithCards(nextPlayer);

        turnRepository.save(nextTurn);

        // Set a foreign key to the current turn in the game
        game.getTurns().add(nextTurn);
    }
}
