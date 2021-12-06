package org.springframework.ntfh.entity.enemy.hordeenemy.ingame;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.enemy.EnemyLocation;
import org.springframework.ntfh.entity.enemy.hordeenemy.HordeEnemy;
import org.springframework.ntfh.entity.enemy.hordeenemy.HordeEnemyService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andrsdt
 * @author alegestor
 */
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
    public Iterable<HordeEnemyIngame> findHordeEnemyByGameId(Integer gameId) {
        return hordeEnemyIngameRepository.findByGameId(gameId);
    }

    @Transactional
    public void save(HordeEnemyIngame hordeEnemyIngame) throws DataAccessException {
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

        List<HordeEnemy> allHordeEnemies = StreamSupport.stream(hordeEnemyService.findAll().spliterator(), false)
                .collect(Collectors.toList());

        Collections.shuffle(allHordeEnemies);

        allHordeEnemies.stream() // From the shuffled list of possible horde enemies
                .limit(numEnemies.get(numPlayers)) // Only keep the amount corresponding to the number of players
                .forEach(hordeEnemy -> createFromHordeEnemy(hordeEnemy, game)); // And create the DB row of each one
    }

    @Transactional
    public void createFromHordeEnemy(HordeEnemy hordeEnemy, Game game) {
        HordeEnemyIngame hordeEnemyIngame = new HordeEnemyIngame();
        hordeEnemyIngame.setHordeEnemy(hordeEnemy);
        hordeEnemyIngame.setCurrentEndurance(hordeEnemy.getEndurance());
        hordeEnemyIngame.setHordeEnemyLocation(EnemyLocation.PILE);
        hordeEnemyIngame.setGame(game);
        this.save(hordeEnemyIngame);
    }

    // TODO refactor, hard to understand. Do we really need the if-else? The
    // endurance is set to the base hordeEnemy's endurance when it's created.
    @Transactional
    public void editHordeEnemyEndurance(HordeEnemyIngame hordeEnemyIngame, Integer damage) {
        if (hordeEnemyIngame.getCurrentEndurance().equals(hordeEnemyIngame.getHordeEnemy().getEndurance()))
            hordeEnemyIngame.setCurrentEndurance(hordeEnemyIngame.getHordeEnemy().getEndurance() - damage);
        else
            hordeEnemyIngame.setCurrentEndurance(hordeEnemyIngame.getCurrentEndurance() - damage);
    }

}
