package org.springframework.ntfh.entity.user;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    // TODO is @Query needed?
    @Query("select u from User u")
    Page<User> findAllPage(Pageable pageable);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    // User with more games played (list with 1 element)
    @Query("SELECT u from User u inner join u.players ps ORDER BY SIZE(ps) DESC")
    List<User> maxNumberOfGamesPlayed(Pageable pageable);

    // User with more games won (list with 1 element)
    @Query("SELECT u from User u inner join u.players ps WHERE ps.game.winner = ps ORDER BY SIZE(ps) DESC")
    List<User> maxNumberOfGamesWon(Pageable pageable);

}
