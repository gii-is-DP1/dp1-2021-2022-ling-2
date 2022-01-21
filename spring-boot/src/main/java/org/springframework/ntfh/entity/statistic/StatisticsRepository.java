package org.springframework.ntfh.entity.statistic;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.user.User;

public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {

    Iterable<Statistics> findByuser(User user);

    // ********For Global Statistics***********

    // Duration
    @Query("Select SUM(s.duration) FROM Statistics s")
    Integer globalDurationOfGames();

    @Query("SELECT u.username, COUNT(g) as wins FROM User u JOIN u.players ps JOIN ps.game g WHERE g.winner.user = u GROUP BY u.username ORDER BY wins DESC")
    List<List<Object>> rankingByWins(Pageable pageable);

    @Query("SELECT s.user.username, SUM(s.gloryEarned) as glory FROM Statistics s GROUP BY s.user.username ORDER BY glory DESC")
    List<List<Object>> rankingByGlory(Pageable pageable);

    @Query("SELECT s.user.username, SUM(s.killCount) as kills FROM Statistics s GROUP BY s.user.username ORDER BY kills DESC")
    List<List<Object>> rankingByKills(Pageable pageable);

    // *********For User Statistics*************

    int countByUser(User user);

    @Query("Select MIN(s.duration) FROM Statistics s WHERE s.user= ?1")
    Integer minDurationOfGames(User user);

    @Query("Select MAX(s.duration) FROM Statistics s WHERE s.user= ?1")
    Integer maxDurationOfGames(User user);

    @Query("Select AVG(s.duration) FROM Statistics s WHERE s.user= ?1")
    Double avgUserDurationOfGames(User user);

    // Wins and Games Queries
    @Query("Select COUNT(s) FROM Statistics s WHERE s.user= ?1")
    Integer findNumberOfGamesByUser(User user);

    @Query("Select COUNT(*) FROM Statistics s Where s.user =?1 and s.victory = TRUE")
    Integer findNumberOfVictoriesByUser(User user);

    // Queries Related To the Characters
    @Query("Select distinct s.character, COUNT(s.character) FROM Statistics s where s.user = ?1 GROUP BY s.character ORDER BY COUNT(s.character)")
    List<List<Object>> listCharactersPlayedByUser(User user);

    @Query("Select COUNT(*) FROM Statistics s Where s.user = ?1 and s.character = ?2")
    Integer findNumberOfGamesWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select COUNT(*) FROM Statistics s Where s.user = ?1 and s.character = ?2 and s.victory = TRUE")
    Integer findNumberOfWinsWithCharacter(User user, CharacterTypeEnum character);

    // KillCOUNT Related Queries
    @Query("Select SUM(s.killCount) FROM Statistics s Where s.user =?1")
    Integer findTotalNumberOfkillsByUser(User user);

    // GloryEarned Related Queries
    @Query("Select SUM(s.gloryEarned) FROM Statistics s Where s.user =?1")
    Integer findTotalGloryPointsByUser(User user);
}
