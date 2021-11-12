package org.springframework.samples.ntfh.game;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepo;

    @Transactional
    public Integer gameCount(){
        return (int) gameRepo.count();
    }
    
}
