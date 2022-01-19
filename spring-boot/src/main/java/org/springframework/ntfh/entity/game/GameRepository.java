package org.springframework.ntfh.entity.game;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;
import org.springframework.ntfh.entity.user.User;

public interface GameRepository extends CrudRepository<Game, Integer> {
    // TODO maybe we don't need a custom query for this, just name with
    // (...sortById)
    /**
     * @see https://stackoverflow.com/questions/20679237/jpql-limit-query
     */
    @Query("SELECT g.turns FROM Game g WHERE g.id = ?1 ORDER BY g.id")
    List<Turn> getTurnsByGameId(Integer gameId);

    @Query("SELECT g.players FROM Game g WHERE g.id = ?1")
    List<Player> getPlayersByGameId(int gameId);

    List<Game> findByStateType(GameStateType stateType);

    int countByStateType(GameStateType stateType);

    @Query("SELECT distinct g from Game g inner join g.players ps where ps.user = ?1 and g.stateType = ?2")
    List<Game> findFinishedByUser(User user, GameStateType stateType);

    @Query("SELECT COUNT(distinct g) from Game g inner join g.players ps where ps.user = ?1 and g.stateType = ?2")
    Integer countFinishedByUser(User user, GameStateType stateType);

}
