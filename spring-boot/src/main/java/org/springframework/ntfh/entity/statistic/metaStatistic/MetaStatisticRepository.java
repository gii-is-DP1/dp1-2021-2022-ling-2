package org.springframework.ntfh.entity.statistic.metaStatistic;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.character.CharacterTypeEnum;
import org.springframework.ntfh.entity.user.User;

public interface MetaStatisticRepository extends CrudRepository<MetaStatistic, Integer> {


    // ********For Global Statistics***********

    // Number of games
    @Query("Select COUNT(g) FROM Game g")
    Integer numberOfGamesTotal();

    @Query("SELECT u from User u inner join u.players ps ORDER BY SIZE(ps) DESC")
    List<User> listUserByNumberOfGames(Pageable pageable);


    // Duration
    @Query("Select SUM(MS.duration) FROM MetaStatistic MS")
    Integer globalDurationOfGames();


    // *********For User Statistics*************

    // Duration Related Queries
    @Query("Select AVG(MS.duration) FROM MetaStatistic MS WHERE MS.user= ?1")
    Double avgUserDurationOfGames(User user);

    @Query("Select MIN(MS.duration) FROM MetaStatistic MS WHERE MS.user= ?1")
    Integer minDurationOfGames(User user);

    @Query("Select Max(MS.duration) FROM MetaStatistic MS WHERE MS.user= ?1")
    Integer maxDurationOfGames(User user);

    // Wins and Games Queries
    @Query("Select COUNT(ms) FROM MetaStatistic ms WHERE ms.user= ?1")
    Integer findNumberOfGamesByUser(User user);

    @Query("Select Count(*) FROM MetaStatistic Ms Where Ms.user =?1 and Ms.victory='true' ")
    Integer findNumberOfVictoriesByUser(User user);

    // Queries Related To the Characters
    @Query("Select distinct Ms.character, Count(Ms.character) FROM MetaStatistic Ms where Ms.user = ?1 GROUP BY Ms.character ORDER BY Count(Ms.character)")
    List<List<Object>> listCharactersPlayedByUser(User user);

    @Query("Select Count(*) FROM MetaStatistic Ms Where Ms.user = ?1 and Ms.character = ?2")
    Integer findNumberOfGamesWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select Count(*) FROM MetaStatistic Ms Where Ms.user = ?1 and Ms.character = ?2 and Ms.victory=True")
    Integer findNumberOfWinsWithCharacter(User user, CharacterTypeEnum character);

    // Queries Death Related
    @Query("Select Count(*) From MetaStatistic Ms Where Ms.user = ?1 and Ms.died='true'")
    Integer findNumberOfDeathsbyUser(User user);

    @Query("Select Count(*) From MetaStatistic Ms Where Ms.user =?1 and Ms.character=?2")
    Integer findNumberOfDeathsbyUserWithCharacter(User user, CharacterTypeEnum character);

    // KillCount Related Queries
    @Query("Select SUM(Ms.killCount) FROM MetaStatistic Ms Where Ms.user =?1")
    Integer findTotalNumberOfkillsByUser(User user);

    @Query("Select MAX(Ms.killCount) FROM MetaStatistic Ms Where Ms.user =?1")
    Integer findMaxNumberOfkillsByUser(User user);

    @Query("Select AVG(Ms.killCount) FROM MetaStatistic Ms Where Ms.user =?1")
    Double findAVGNumberOfkillsByUser(User user);

    @Query("Select SUM(Ms.killCount) FROM MetaStatistic Ms Where Ms.user =?1 and Ms.character =?2")
    Integer findTotalNumberOfkillsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select MAX(Ms.killCount) FROM MetaStatistic Ms Where Ms.user =?1 and Ms.character =?2")
    Integer findMaxNumberOfkillsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select AVG(Ms.killCount) FROM MetaStatistic Ms Where Ms.user =?1 and Ms.character =?2")
    Double findAVGNumberOfkillsByUserWithCharacter(User user, CharacterTypeEnum character);


    // GloryEarned Related Queries
    @Query("Select SUM(Ms.gloryEarned) FROM MetaStatistic Ms Where Ms.user =?1")
    Integer findTotalGloryPointsByUser(User user);

    @Query("Select MAX(Ms.gloryEarned) FROM MetaStatistic Ms Where Ms.user =?1")
    Integer findMaxGloryPointsByUser(User user);

    @Query("Select AVG(Ms.gloryEarned) FROM MetaStatistic Ms Where Ms.user =?1")
    Double findAVGGloryPointsByUser(User user);

    @Query("Select SUM(Ms.gloryEarned) FROM MetaStatistic Ms Where Ms.user =?1 and Ms.character =?2")
    Integer findTotalGloryPointsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select MAX(Ms.gloryEarned) FROM MetaStatistic Ms Where Ms.user =?1 and Ms.character =?2")
    Integer findMaxGloryPointsByUserWithCharacter(User user, CharacterTypeEnum character);

    @Query("Select AVG(Ms.gloryEarned) FROM MetaStatistic Ms Where Ms.user =?1 and Ms.character =?2")
    Double findAVGGloryPointsByUserWithCharacter(User user, CharacterTypeEnum character);



}
