package org.springframework.ntfh.entity.turn;

import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.scene.Scene;
import org.springframework.ntfh.entity.scene.SceneService;
import org.springframework.stereotype.Service;

@Service
public class TurnService {

    @Autowired
    private TurnRepository turnRepository;

    @Autowired
    private SceneService sceneService;

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
     * Create a turn given a game
     * 
     * @author andrsdt
     * @param game that the turn will be created for
     */
    @Transactional
    public void createFromGame(Game game) {
        Turn turn = new Turn();
        turn.setPlayer(game.getLeader()); // TODO create a Integer turnOrder in Player, a transient getter

        if (game.getHasScenes()) {
            // Get a random scene and set it as the current scene
            Scene randomScene = sceneService.findSceneById(new Random().nextInt(sceneService.count())).get();
            turn.setCurrentScene(randomScene);
        }

        turn.setGame(game);
        turnRepository.save(turn);

        // Set a foreign key to the current turn in the game
        game.setCurrentTurn(turn);
    }

    // State functions

    

}
