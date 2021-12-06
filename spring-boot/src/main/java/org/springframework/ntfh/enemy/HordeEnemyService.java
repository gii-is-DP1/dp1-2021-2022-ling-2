package org.springframework.ntfh.enemy;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HordeEnemyService {

    @Autowired
    private HordeEnemyRepository hordeEnemyRepository;

    @Transactional
    public Integer hordeEnemyCount() {
        return (int) hordeEnemyRepository.count();
    }

    @Transactional
    public Iterable<HordeEnemy> findAll() {
        return hordeEnemyRepository.findAll();
    }

    @Transactional
    public Optional<HordeEnemy> findHordeEnemyById(Integer id) {
        return hordeEnemyRepository.findById(id);
    }

}
