package org.springframework.ntfh.enemy.ingame;

import org.springframework.data.repository.CrudRepository;

public interface HordeEnemyIngameRepository extends CrudRepository<HordeEnemyIngame, Integer> {
    Iterable<HordeEnemyIngame> findByGameId(Integer gameId);
}
