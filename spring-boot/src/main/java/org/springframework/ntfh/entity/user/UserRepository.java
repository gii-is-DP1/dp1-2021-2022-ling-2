package org.springframework.ntfh.entity.user;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u from User u")
    Page<User> findAllPage(Pageable pageable);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    // User with more games played (list with 1 element)
    @Query("SELECT u from User u inner join u.players ps ORDER BY SIZE(ps) DESC")
    Page<User> maxNumberOfGamesPlayed(Pageable pageable);

    // User with more games won (list with 1 element)
    @Query("SELECT u from User u inner join u.players ps WHERE ps.game.winner = ps ORDER BY SIZE(ps) DESC")
    Page<User> maxNumberOfGamesWon(Pageable pageable);

    // Query to get the users ordered by number of kills
    @Query("SELECT u.username, SUM(ps.kills) AS kills from User u inner join u.players ps WHERE ps.user = u GROUP BY u ORDER BY kills DESC")
    Page<Object> rankingByTotalKills(Pageable pageable);

    // Query to get the users ordered by number of kills
    @Query("SELECT u.username, SUM(ps.glory) AS glory from User u inner join u.players ps WHERE ps.user = u GROUP BY u ORDER BY glory DESC")
    Page<Object> rankingByTotalGlory(Pageable pageable);

    // Query to get the user with most won games
    @Query("SELECT u from User u inner join u.players ps WHERE ps.game.winner = ps GROUP BY u ORDER BY SIZE(ps) DESC")
    Page<User> rankingByWonGames(Pageable pageable);

}
