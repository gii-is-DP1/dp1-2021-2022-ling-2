package org.springframework.ntfh.entity.enemy.hordeenemy.ingame;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.enemy.EnemyLocation;
import org.springframework.ntfh.entity.enemy.hordeenemy.HordeEnemy;
import org.springframework.ntfh.entity.enemy.hordeenemy.HordeEnemyService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
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

    @Autowired
    private GameService gameService;

    @Transactional
    public Integer hordeEnemyIngameCount() {
        return (int) hordeEnemyIngameRepository.count();
    }

    @Transactional
    public Iterable<HordeEnemyIngame> findAll() {
        return hordeEnemyIngameRepository.findAll();
    }

    @Transactional
    public HordeEnemyIngame findById(Integer id) throws DataAccessException {
        Optional<HordeEnemyIngame> hordeEnemyIngame = hordeEnemyIngameRepository.findById(id);
        if (!hordeEnemyIngame.isPresent())
            throw new DataAccessException("HordeEnemyIngame with id " + id + " was not found") {
            };
        return hordeEnemyIngame.get();
    }

    @Transactional
    public Iterable<HordeEnemyIngame> findHordeEnemyByGameId(Integer gameId) {
        return gameService.findGameById(gameId).getHordeEnemies();
    }

    @Transactional
    public void save(HordeEnemyIngame hordeEnemyIngame) throws DataAccessException {
        hordeEnemyIngameRepository.save(hordeEnemyIngame);
    }

    /**
     * Keep taking enemies from the pile and adding them to the fighting area while
     * there are less than 3
     * 
     * @author @andrsdt
     */
    private void refillTableWithEnemies(Game game) {
        List<HordeEnemyIngame> hordeEnemiesIngame = game.getHordeEnemies();
        List<HordeEnemyIngame> enemiesInPile = hordeEnemiesIngame.stream()
                .filter(e -> e.getHordeEnemyLocation() == EnemyLocation.PILE).collect(Collectors.toList());
        List<HordeEnemyIngame> enemiesFighting = hordeEnemiesIngame.stream()
                .filter(e -> e.getHordeEnemyLocation() == EnemyLocation.FIGHTING).collect(Collectors.toList());

        while (!enemiesInPile.isEmpty() && enemiesFighting.size() < 3) {
            // The game rules tell us that the horde enemy cards have to be taken from the
            // bottom of the pile
            HordeEnemyIngame lastEnemyInPile = enemiesInPile.get(0);
            enemiesInPile.remove(lastEnemyInPile);
            lastEnemyInPile.setHordeEnemyLocation(EnemyLocation.FIGHTING);
            enemiesFighting.add(lastEnemyInPile);
        }

        // Join both lists back together and save them in the DB
        List<HordeEnemyIngame> enemiesInPileAndFighting = Stream.of(enemiesInPile, enemiesFighting)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        game.setHordeEnemies(enemiesInPileAndFighting);

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
    public void initializeFromGame(Game game) {
        Integer numPlayers = game.getPlayers().size();
        Map<Integer, Integer> numEnemies = Map.of(
                2, 19, // 19 horde enemies for 2 players
                3, 23, // 23 horde enemies for 3 players
                4, 27);// 27 horde enemies for 4 players

        List<HordeEnemy> allHordeEnemies = StreamSupport.stream(hordeEnemyService.findAll().spliterator(), false)
                .collect(Collectors.toList());

        Collections.shuffle(allHordeEnemies);

        List<HordeEnemyIngame> gameHordeEnemies = allHordeEnemies.stream() // From the shuffled list of possible enemies
                .limit(numEnemies.get(numPlayers)) // Only keep the amount corresponding to the number of players
                .map(hordeEnemy -> createFromHordeEnemy(hordeEnemy, game)) // And create the DB row of each one
                .collect(Collectors.toList());

        game.setHordeEnemies(gameHordeEnemies);

        refillTableWithEnemies(game);
    }

    @Transactional
    public HordeEnemyIngame createFromHordeEnemy(HordeEnemy hordeEnemy, Game game) {
        HordeEnemyIngame hordeEnemyIngame = new HordeEnemyIngame();
        hordeEnemyIngame.setHordeEnemy(hordeEnemy);
        hordeEnemyIngame.setCurrentEndurance(hordeEnemy.getEndurance());
        hordeEnemyIngame.setHordeEnemyLocation(EnemyLocation.PILE);
        // All the horde enemies will be on the pile initially. When the first turn
        // begins, the refill of enemies for fight will be done (by the Turn service?)
        this.save(hordeEnemyIngame);
        return hordeEnemyIngame;
    }
}
