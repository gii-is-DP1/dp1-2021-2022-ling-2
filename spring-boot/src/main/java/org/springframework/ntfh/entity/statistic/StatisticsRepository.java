package org.springframework.ntfh.entity.statistic;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.user.User;

public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {


    // ********For Global Statistics***********

    @Query("SELECT u from User u inner join u.players ps ORDER BY SIZE(ps) DESC")
    List<User> listUserByNumberOfGames(Pageable pageable);

    // Duration
    @Query("Select SUM(s.duration) FROM Statistics s")
    Integer globalDurationOfGames();

    @Query("SELECT u.username, COUNT(g) as wins FROM User u JOIN u.players ps JOIN ps.game g WHERE g.winner.user = u GROUP BY u.username ORDER BY wins DESC")
    List<List<Object>> rankingByWins(Pageable pageable);

    @Query("SELECT u.username, COUNT(g) as glory FROM User u JOIN u.players ps JOIN ps.game g WHERE g.winner.user = u GROUP BY u.username ORDER BY glory DESC")
    List<List<Object>> rankingByGlory(Pageable pageable);

    @Query("SELECT u.username, COUNT(g) as kills FROM User u JOIN u.players ps JOIN ps.game g WHERE g.winner.user = u GROUP BY u.username ORDER BY kills DESC")
    List<List<Object>> rankingByKills(Pageable pageable);

    // *********For User Statistics*************

    int countByUser(User user);

    // Duration Related Queries

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

    // Death Related Queries
    @Query("Select COUNT(*) From Statistics s Where s.user = ?1 and s.died = TRUE")
    Integer findNumberOfDeathsbyUser(User user);

    @Query("Select COUNT(*) From Statistics s Where s.user =?1 and s.character=?2")
    Integer findNumberOfDeathsbyUserWithCharacter(User user, CharacterTypeEnum character);

    // KillCOUNT Related Queries
    @Query("Select SUM(s.killCount) FROM Statistics s Where s.user =?1")
    Integer findTotalNumberOfkillsByUser(User user);

    @Query("Select MAX(s.killCount) FROM Statistics s Where s.user =?1")
    Integer findMaxNumberOfkillsByUser(User user);

    @Query("Select AVG(s.killCount) FROM Statistics s Where s.user =?1")
    Double findAVGNumberOfkillsByUser(User user);

    @Query("Select SUM(s.killCount) FROM Statistics s Where s.user =?1 and s.character =?2")
    Integer findTotalNumberOfkillsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select MAX(s.killCount) FROM Statistics s Where s.user =?1 and s.character =?2")
    Integer findMaxNumberOfkillsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select AVG(s.killCount) FROM Statistics s Where s.user =?1 and s.character =?2")
    Double findAVGNumberOfkillsByUserWithCharacter(User user, CharacterTypeEnum character);


    // GloryEarned Related Queries
    @Query("Select SUM(s.gloryEarned) FROM Statistics s Where s.user =?1")
    Integer findTotalGloryPointsByUser(User user);

    @Query("Select MAX(s.gloryEarned) FROM Statistics s Where s.user =?1")
    Integer findMaxGloryPointsByUser(User user);

    @Query("Select AVG(s.gloryEarned) FROM Statistics s Where s.user =?1")
    Double findAVGGloryPointsByUser(User user);

    @Query("Select SUM(s.gloryEarned) FROM Statistics s Where s.user =?1 and s.character =?2")
    Integer findTotalGloryPointsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select MAX(s.gloryEarned) FROM Statistics s Where s.user =?1 and s.character =?2")
    Integer findMaxGloryPointsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select AVG(s.gloryEarned) FROM Statistics s Where s.user =?1 and s.character =?2")
    Double findAVGGloryPointsByUserWithCharacter(User user, CharacterTypeEnum character);
}
