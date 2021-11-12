package org.springframework.samples.ntfh.game;

import org.springframework.data.repository.CrudRepository;


public interface GameRepository extends CrudRepository<GameEntity,Integer>{
    
}
