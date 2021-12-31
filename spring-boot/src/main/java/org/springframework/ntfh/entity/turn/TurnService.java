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
        PlayerState initialState = new PlayerState(turn);
        turn.setPlayer(game.getLeader());
        turn.setStage(TurnStageEnum.PLAYER_ATTACK);

        if (game.getHasScenes()) {
            // Get a random scene and set it as the current scene
            Scene randomScene = sceneService
                    .findSceneById(new Random().nextInt(sceneService.count()) + 1).get(); // DB indexes start in 1
            turn.setCurrentScene(randomScene);
        }

        turn.setGame(game); // TODO needed?
        turn.setState(initialState);
        enemyIngameService.initializeFromGame(game);
        marketCardIngameService.initializeFromGame(game);
        abilityCardIngameService.initializeFromGame(game);
        turnRepository.save(turn);

        // Set a foreign key to the current turn in the game
        game.setCurrentTurn(turn);
    }

    // State functions
    @Transactional
    public void stateButton(Turn turn) {
        turn.button();
    }

}
