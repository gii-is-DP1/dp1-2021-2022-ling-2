package org.springframework.samples.ntfh.entity.game.history;

import org.springframework.data.repository.CrudRepository;

public interface GameHistoryRepository extends CrudRepository<GameHistory, Integer> {
    // TODO make this work
    Iterable<GameHistory> findByGamePlayersContaining(String username);
}
