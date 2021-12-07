package org.springframework.ntfh.entity.enemy.hordeenemy.ingame;

import org.springframework.data.repository.CrudRepository;

public interface HordeEnemyIngameRepository extends CrudRepository<HordeEnemyIngame, Integer> {
    Iterable<HordeEnemyIngame> findByGameId(Integer gameId);
}
