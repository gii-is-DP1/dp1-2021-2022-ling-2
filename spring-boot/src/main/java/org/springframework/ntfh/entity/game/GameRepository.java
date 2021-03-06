package org.springframework.ntfh.entity.game;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.user.User;

public interface GameRepository extends CrudRepository<Game, Integer> {
    /**
     * @see https://stackoverflow.com/questions/20679237/jpql-limit-query
     */
    @Query("SELECT g.turns FROM Game g WHERE g.id = ?1 ORDER BY g.id")
    List<Turn> getTurnsByGameId(Integer gameId);

    @Query("SELECT g.players FROM Game g WHERE g.id = ?1")
    List<Player> getPlayersByGameId(int gameId);

    Page<Game> findByStateType(GameStateType stateType, Pageable pageable);

    Integer countByStateType(GameStateType stateType);

    @Query("SELECT distinct g from Game g inner join g.players ps where ps.user = ?1 and g.stateType = ?2")
    Page<Game> findFinishedByUser(User user, GameStateType stateType, Pageable pageable);
}
