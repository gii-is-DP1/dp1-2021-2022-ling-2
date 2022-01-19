package org.springframework.ntfh.entity.statistic;

import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.user.User;

public interface StatisticRepostory extends CrudRepository<Statistic,Integer> {
 
    // Number of Games
    @Query("Select Count(*) from Game g")
    Integer findNumOfGamesGlobal();

    @Query("Select Count(*) distinct g from Game g inner join g.players ps where ps.user = ?1")
    Integer findNumOfGamesByUser(User username);
    
    @Query("Select MAX(st.matches) FROM Statistics st")
    Integer findMaxNumberOfGamesByOneUser();

    @Query("Select Min(st.matches) FROM Statistics st")
    Integer findMinNumberOfGamesByOneUser();

    @Query("Select AVG(st.matches) FROM Statistics st")
    Integer findAvgNumberOfGamesByUser();
    
    // Duration of Games
    @Query("Select Sum(g.finishTime-g.startTime) FROM GAME g WHERE g.stateType='FINISHED' ")
    Integer findTotalTimePlayed();

    @Query("Select Sum(g.finishTime-g.startTime) FROM GAME g inner g.players ps where ps.user = ?1")
    Integer findTimePlayedByPlayer(User username);

    @Query("Select MAX(g.finishTime-g.startTime) FROM GAME g inner g.players ps where ps.user = ?1")
    Integer findMaxTimePlayedByPlayer(User username);

    @Query("Select Min(g.finishTime-g.startTime) FROM GAME g inner g.players ps where ps.user = ?1")
    Integer findMinTimePlayedByPlayer(User username);

    @Query("Select AVG(g.finishTime-g.startTime) FROM GAME g inner g.players ps where ps.user = ?1")
    Integer findAvgTimePlayedByPlayer(User username);


    // Num of players per game
    @Query("Select g.maxPlayers FROM GAME g WHERE g.id= ?1")
    Integer findNumPlayersPerGame(Integer gameId);

    // A map of games with 2 3 or 4 users could by really usefull
    Map<Integer,Integer> aux();


    // Ranking
}
