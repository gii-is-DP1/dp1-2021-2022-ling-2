package org.springframework.ntfh.entity.playablecard.marketcard;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketCardService {

    @Autowired
    private MarketCardRepository marketCardRepository;

    public Integer marketCardCount() {
        return (int) marketCardRepository.count();
    }

    public Iterable<MarketCard> findAll() {
        return marketCardRepository.findAll();
    }

    // TODO throw exception instead of using optional
    public Optional<MarketCard> findMarketCardById(Integer id) {
        return marketCardRepository.findById(id);
    }

    // Only GET methods are available because all cards data will be injected from
    // the data.sql file

}
