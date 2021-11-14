package org.springframework.samples.ntfh.game;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

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
    
    public Iterable<GameEntity> findAll(){
        return gameRepo.findAll();
    }

    @Transactional
    public Optional<GameEntity> findGameById(int id){
        return gameRepo.findById(id);
    }

    @Transactional
    public void save(@Valid GameEntity game) {
        gameRepo.save(game);
    }

    public void delete(GameEntity game) {
        gameRepo.delete(game);
    }

}
