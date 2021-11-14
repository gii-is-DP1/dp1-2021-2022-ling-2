package org.springframework.samples.ntfh.enemy;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HordeEnemyService {
    @Autowired
    private HordeEnemyRepository hordeEnemyRepository;

    @Transactional
    public Iterable<HordeEnemy> findAll() {
        return hordeEnemyRepository.findAll();
    }

    @Transactional
    public Optional<HordeEnemy> findHordeEnemyById(Integer id) {
        return hordeEnemyRepository.findById(id);
    }

    // Only GET methods are provided because all HordeEnemy data will be injected
    // via .sql file
}