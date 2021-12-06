package org.springframework.ntfh.entity.marketcard.ingame;

import org.springframework.data.repository.CrudRepository;

public interface MarketCardIngameRepository extends CrudRepository<MarketCardIngame, Integer> {
    Iterable<MarketCardIngame> findByGameId(Integer gameId);
}
