package org.springframework.ntfh.entity.game;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.player.Player;
import org.springframework.ntfh.entity.turn.Turn;

public interface GameRepository extends CrudRepository<Game, Integer> {
    @Query("SELECT g.currentTurn FROM Game g WHERE g.id = ?1")
    Turn getCurrentTurnByGameId(Integer gameId);

    @Query("SELECT g.players FROM Game g WHERE g.id = ?1")
    List<Player> getPlayersByGameId(int gameId);
}
