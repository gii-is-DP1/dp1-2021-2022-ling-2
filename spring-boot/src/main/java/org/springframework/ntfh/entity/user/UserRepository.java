package org.springframework.ntfh.entity.user;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    // TODO is @Query needed?
    @Query("select u from User u")
    Page<User> findAllPage(Pageable pageable);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findByUsername(String username);

    // User with more games played (list with 1 element)
    @Query("SELECT u from User u inner join u.players ps ORDER BY SIZE(ps) DESC")
    List<User> maxNumberOfGamesPlayed(Pageable pageable);

    // TODO return List<Object> with both user and stats
    // User with more games won (list with 1 element)
    @Query("SELECT u from User u inner join u.players ps WHERE ps.game.winner = ps ORDER BY SIZE(ps) DESC")
    List<User> maxNumberOfGamesWon(Pageable pageable);

    // Query to get the users ordered by number of kills
    @Query("SELECT u.username, SUM(ps.kills) AS kills from User u inner join u.players ps WHERE ps.user = u GROUP BY u ORDER BY kills DESC")
    List<Object> rankingByTotalKills(Pageable pageable);

    // Query to get the users ordered by number of kills
    @Query("SELECT u.username, SUM(ps.glory) AS glory from User u inner join u.players ps WHERE ps.user = u GROUP BY u ORDER BY glory DESC")
    List<Object> rankingByTotalGlory(Pageable pageable);

    // Query to get the user with most won games
    @Query("SELECT u from User u inner join u.players ps WHERE ps.game.winner = ps GROUP BY u ORDER BY SIZE(ps) DESC")
    List<User> rankingByWonGames(Pageable pageable);

}
