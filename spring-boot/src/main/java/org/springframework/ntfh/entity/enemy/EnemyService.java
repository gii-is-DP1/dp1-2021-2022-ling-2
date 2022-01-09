package org.springframework.ntfh.entity.enemy;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnemyService {

    @Autowired
    private EnemyRepository enemyRepository;

    public Integer count() {
        return (int) enemyRepository.count();
    }

    public Iterable<Enemy> findAll() {
        return enemyRepository.findAll();
    }

    public Optional<Enemy> findEnemyById(Integer id) {
        return enemyRepository.findById(id);
    }

    public List<Enemy> findByEnemyCategoryType(EnemyCategoryType enemyCategoryType) {
        return enemyRepository.findByEnemyCategoryType(enemyCategoryType);
    }
}
