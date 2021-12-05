package org.springframework.samples.ntfh.enemy.ingame;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.ntfh.enemy.HordeEnemy;
import org.springframework.samples.ntfh.enemy.HordeEnemyService;
import org.springframework.samples.ntfh.game.Game;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HordeEnemyIngameService {

    @Autowired
    private HordeEnemyIngameRepository hordeEnemyIngameRepository;

    @Autowired
    private HordeEnemyService hordeEnemyService;

    @Transactional
    public Integer hordeEnemyIngameCount() {
        return (int) hordeEnemyIngameRepository.count();
    }

    @Transactional
    public Iterable<HordeEnemyIngame> findAll() {
        return hordeEnemyIngameRepository.findAll();
    }

    @Transactional
    public Optional<HordeEnemyIngame> findHordeEnemyIngameById(Integer id) {
        return hordeEnemyIngameRepository.findById(id);
    }

    @Transactional
    public void saveHordeEnemyIngame(HordeEnemyIngame hordeEnemyIngame) throws DataAccessException {
        hordeEnemyIngameRepository.save(hordeEnemyIngame);
    }

    /**
     * Given a game, create the initial horde enemies. These will be random and the
     * number of enemies will depend on the number of players
     * 
     * @author andrsdt
     * @param hordeEnemy
     * @param hordeEnemyLocation
     * @return
     */
    @Transactional
    public void createFromGame(Game game) {
        Integer numPlayers = game.getPlayers().size();
        Map<Integer, Integer> numEnemies = Map.of(
                2, 19, // 19 horde enemies for 2 players
                3, 23, // 23 horde enemies for 3 players
                4, 27);// 27 horde enemies for 4 players

        Iterable<HordeEnemy> allHordeEnemies = hordeEnemyService.findAll();
        Set<HordeEnemy> hordeEnemies = StreamSupport.stream(allHordeEnemies.spliterator(), false)
                .limit(numEnemies.get(numPlayers)) // Take the ammount corresponding to the number of players
                .collect(Collectors.toSet()); // Set of the selected hordeEnemies

        Set<HordeEnemyIngame> hordeEnemyIngames = hordeEnemies.stream()
                .map(hordeEnemy -> createFromHordeEnemy(hordeEnemy, game)) // Create a hordeEnemyIngame for every
                                                                           // hordeEnemy
                .collect(Collectors.toSet());

        // Store each one of them in the database
        hordeEnemyIngames.forEach(hordeEnemyIngame -> {
            try {
                saveHordeEnemyIngame(hordeEnemyIngame);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional
    public HordeEnemyIngame createFromHordeEnemy(HordeEnemy hordeEnemy, Game game) {
        HordeEnemyIngame hordeEnemyIngame = new HordeEnemyIngame();
        hordeEnemyIngame.setHordeEnemy(hordeEnemy);
        hordeEnemyIngame.setCurrentEndurance(hordeEnemy.getEndurance());
        hordeEnemyIngame.setHordeEnemyLocation(EnemyLocation.PILE);
        hordeEnemyIngame.setGame(game);
        return hordeEnemyIngameRepository.save(hordeEnemyIngame);
    }

    // TODO refactor, hard to understand at first sight
    @Transactional
    public void editHordeEnemyEndurance(HordeEnemyIngame hordeEnemyIngame, Integer damage) {
        if (hordeEnemyIngame.getCurrentEndurance().equals(hordeEnemyIngame.getHordeEnemy().getEndurance()))
            hordeEnemyIngame.setCurrentEndurance(hordeEnemyIngame.getHordeEnemy().getEndurance() - damage);
        else
            hordeEnemyIngame.setCurrentEndurance(hordeEnemyIngame.getCurrentEndurance() - damage);
    }

}
