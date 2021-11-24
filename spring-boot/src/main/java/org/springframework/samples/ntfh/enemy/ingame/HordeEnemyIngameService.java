package org.springframework.samples.ntfh.enemy.ingame;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.ntfh.enemy.HordeEnemy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HordeEnemyIngameService {

    @Autowired
    private HordeEnemyIngameRepository hordeEnemyIngameRepository;

    @Transactional
    public Integer hordeEnemyIngameCount() {
        return (int) hordeEnemyIngameRepository.count();
    }

    @Transactional
    public Iterable<HordeEnemyIngame> findAll() {
        return hordeEnemyIngameRepository.findAll();
    }

    @Transactional
    public Optional<HordeEnemyIngame> findHordeEnemyIngameById(Integer id) {
        return hordeEnemyIngameRepository.findById(id);
    }    
    
    @Transactional
    public void saveWarlordIngame(HordeEnemyIngame hordeEnemyIngame) throws DataAccessException {
        hordeEnemyIngameRepository.save(hordeEnemyIngame);
    }

    @Transactional
    public HordeEnemyIngame createFromHordeEnemy(HordeEnemy hordeEnemy, EnemyLocation hordeEnemyLocation) {
        HordeEnemyIngame hordeEnemyIngame = new HordeEnemyIngame();
        hordeEnemyIngame.setHordeEnemy(hordeEnemy);
        hordeEnemyIngame.setCurrentEndurance(hordeEnemy.getEndurance());
        hordeEnemyIngame.setHordeEnemyLocation(hordeEnemyLocation);
        return hordeEnemyIngameRepository.save(hordeEnemyIngame);
    }

    @Transactional
    public void editHordeEnemyEndurance(HordeEnemyIngame hordeEnemyIngame, Integer damage) {
        if (hordeEnemyIngame.getCurrentEndurance().equals(hordeEnemyIngame.getHordeEnemy().getEndurance())) 
                hordeEnemyIngame.setCurrentEndurance(hordeEnemyIngame.getHordeEnemy().getEndurance() - damage);
        else hordeEnemyIngame.setCurrentEndurance(hordeEnemyIngame.getCurrentEndurance() - damage);
    }
    
}
