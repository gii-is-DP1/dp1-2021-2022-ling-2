package org.springframework.ntfh.entity.turn;

import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.enemy.ingame.EnemyIngameService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.playablecard.abilitycard.ingame.AbilityCardIngameService;
import org.springframework.ntfh.entity.playablecard.marketcard.ingame.MarketCardIngameService;
import org.springframework.ntfh.entity.scene.Scene;
import org.springframework.ntfh.entity.scene.SceneService;
import org.springframework.ntfh.entity.turn.concretestates.EnemyState;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.entity.turn.concretestates.RefreshState;
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

    @Autowired
    private EnemyState enemyState;

    @Autowired
    private RefreshState refreshState;

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
                    .findSceneById(new Random().nextInt(sceneService.count()) + 1).get(); // DB indexes start in 1
            turn.setCurrentScene(randomScene);
        }

        turn.setGame(game); // TODO needed?

        // Initial state is the state where the player attacks
        turn.setStateType(TurnStateType.PLAYER_STATE);

        enemyIngameService.initializeFromGame(game);
        marketCardIngameService.initializeFromGame(game);
        abilityCardIngameService.initializeFromGame(game);
        turnRepository.save(turn);

        // Set a foreign key to the current turn in the game
        game.setCurrentTurn(turn);
    }

    public TurnState getState(Turn turn) {
        TurnStateType stateType = turn.getStateType();
        if (stateType == TurnStateType.PLAYER_STATE) {
            return playerState;
        } else if (stateType == TurnStateType.MARKET_STATE) {
            return marketState;
        } else if (stateType == TurnStateType.ENEMY_STATE) {
            return enemyState;
        } else if (stateType == TurnStateType.REFRESH_STATE) {
            return refreshState;
        } else {
            return null;
        }
    }

    public void setNextState(Turn turn) {
        TurnState state = getState(turn);
        TurnStateType nextState = state.getNextState();
        turn.setStateType(nextState);
    }

    // TODO method to create a new turn (not the intial one)
    // In such a method, make sure to set the list of cards played by the player to
    // an empty list

    // TODO Ending of the turn, make sure to empty the list of cardPlayedInTurn from
    // player (Avoid overlay of effects bettewwnn turns)
}
