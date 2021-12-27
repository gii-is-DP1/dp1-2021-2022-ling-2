package org.springframework.ntfh.entity.enemy;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EnemyRepository extends CrudRepository<Enemy, Integer> {
    List<Enemy> findByEnemyCategoryType(EnemyCategoryType categoryType);
}
