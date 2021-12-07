package org.springframework.ntfh.entity.enemy.warlord.ingame;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.enemy.EnemyLocation;
import org.springframework.ntfh.entity.enemy.warlord.Warlord;
import org.springframework.ntfh.entity.enemy.warlord.WarlordService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author alegestor
 * @author andrsdt
 */
@Service
public class WarlordIngameService {

    @Autowired
    private WarlordIngameRepository warlordIngameRepository;

    @Autowired
    private WarlordService warlordService;

    @Transactional
    public Integer warlordIngameCount() {
        return (int) warlordIngameRepository.count();
    }

    @Transactional
    public Iterable<WarlordIngame> findAll() {
        return warlordIngameRepository.findAll();
    }

    /**
     * @author andrsdt
     * @param gameId
     * @return Warlord of that game
     */
    @Transactional
    public WarlordIngame findWarlordByGameId(Integer gameId) {
        return warlordIngameRepository.findByGameId(gameId);
    }

    @Transactional
    public void saveWarlordIngame(WarlordIngame warlordIngame) throws DataAccessException {
        warlordIngameRepository.save(warlordIngame);
    }

    /**
     * Create a WardlordIngame entity chosen randomly from the list of warlords
     * 
     * @author andrsdt
     * @param game that the warlord will be instantiated for
     */
    @Transactional
    public void createFromGame(Game game) {

        List<Warlord> allWarlords = StreamSupport.stream(warlordService.findAll().spliterator(), false)
                .collect(Collectors.toList());

        Collections.shuffle(allWarlords);
        Warlord randomWarlord = allWarlords.get(0);

        createFromWarlord(randomWarlord, game);
    }

    @Transactional
    public WarlordIngame createFromWarlord(Warlord warlord, Game game) {
        WarlordIngame warlordIngame = new WarlordIngame();
        warlordIngame.setWarlord(warlord);
        warlordIngame.setCurrentEndurance(warlord.getEndurance());
        warlordIngame.setLocation(EnemyLocation.PILE);
        warlordIngame.setGame(game);
        return warlordIngameRepository.save(warlordIngame);
    }

    // TODO refactor, hard to understand. Do we really need the if-else? The
    // endurance is set to the base warlord's endurance when it's created.
    @Transactional
    public void editWarlordEndurance(WarlordIngame warlordIngame, Integer damage) {
        if (warlordIngame.getCurrentEndurance().equals(warlordIngame.getWarlord().getEndurance()))
            warlordIngame.setCurrentEndurance(warlordIngame.getWarlord().getEndurance() - damage);
        else
            warlordIngame.setCurrentEndurance(warlordIngame.getCurrentEndurance() - damage);
    }
}
