package org.springframework.samples.ntfh.game.history;

import org.springframework.data.repository.CrudRepository;

public interface GameHistoryRepository extends CrudRepository<GameHistory, Integer> {
    
}
