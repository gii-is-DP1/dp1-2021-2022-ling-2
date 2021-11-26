package org.springframework.samples.ntfh.game.history;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameHistoryService {
    
    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    @Transactional
    public Iterable<GameHistory> findAll() {
        return gameHistoryRepository.findAll();
    }

    @Transactional
    public Integer count() {
        return (int) gameHistoryRepository.count();
    }
}
