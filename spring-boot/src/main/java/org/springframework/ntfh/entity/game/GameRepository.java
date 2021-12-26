package org.springframework.ntfh.entity.game;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.turn.Turn;

public interface GameRepository extends CrudRepository<Game, Integer> {
    @Query("SELECT g.currentTurn FROM Game g WHERE g.id = ?1")
    Turn getCurrentTurnByGameId(Integer gameId);
}
