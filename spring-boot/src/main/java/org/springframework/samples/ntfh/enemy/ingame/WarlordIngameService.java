package org.springframework.samples.ntfh.enemy.ingame;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.ntfh.enemy.Warlord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarlordIngameService {

    @Autowired
    private WarlordIngameRepository warlordIngameRepository;

    @Transactional
    public Integer warlordIngameCount() {
        return (int) warlordIngameRepository.count();
    }

    @Transactional
    public Iterable<WarlordIngame> findAll() {
        return warlordIngameRepository.findAll();
    }

    @Transactional
    public Optional<WarlordIngame> findWarlordIngameById(Integer id) {
        return warlordIngameRepository.findById(id);
    }    
    
    @Transactional
    public void saveWarlordIngame(WarlordIngame warlordIngame) throws DataAccessException {
        warlordIngameRepository.save(warlordIngame);
    }

    @Transactional
    public WarlordIngame createFromWarlord(Warlord warlord, EnemyLocation warlordLocation) {
        WarlordIngame warlordIngame = new WarlordIngame();
        warlordIngame.setWarlord(warlord);
        warlordIngame.setCurrentEndurance(warlord.getEndurance());
        warlordIngame.setWarlordLocation(warlordLocation);
        return warlordIngameRepository.save(warlordIngame);
    }

    @Transactional
    public void editWarlordEndurance(WarlordIngame warlordIngame, Integer damage) {
        if (warlordIngame.getCurrentEndurance().equals(warlordIngame.getWarlord().getEndurance())) 
                warlordIngame.setCurrentEndurance(warlordIngame.getWarlord().getEndurance() - damage);
        else warlordIngame.setCurrentEndurance(warlordIngame.getCurrentEndurance() - damage);
    }

}
