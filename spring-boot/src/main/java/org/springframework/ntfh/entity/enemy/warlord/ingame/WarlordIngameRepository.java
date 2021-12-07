package org.springframework.ntfh.entity.enemy.warlord.ingame;

import org.springframework.data.repository.CrudRepository;

/**
 * @author alegestor
 * @author andrsdt
 */
public interface WarlordIngameRepository extends CrudRepository<WarlordIngame, Integer> {
    WarlordIngame findByGameId(Integer gameId);
}
