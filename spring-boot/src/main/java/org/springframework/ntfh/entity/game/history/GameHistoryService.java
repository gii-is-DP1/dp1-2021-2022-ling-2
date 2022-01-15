package org.springframework.ntfh.entity.game.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameHistoryService {

    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    public Iterable<GameHistory> findAll() {
        return gameHistoryRepository.findAll();
    }

    public Integer count() {
        return (int) gameHistoryRepository.count();
    }
}
