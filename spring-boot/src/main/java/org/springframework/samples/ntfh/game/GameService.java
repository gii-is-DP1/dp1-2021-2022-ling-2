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
    public Integer gameCount() {
        return (int) gameRepo.count();
    }

    public Iterable<Game> findAll() {
        return gameRepo.findAll();
    }

    @Transactional
    public Optional<Game> findGameById(int id) {
        return gameRepo.findById(id);
    }

    @Transactional
    public Game save(@Valid Game game) {
        // Return the game created after saving it
        return gameRepo.save(game);
    }

    public void delete(Game game) {
        gameRepo.delete(game);
    }

}
