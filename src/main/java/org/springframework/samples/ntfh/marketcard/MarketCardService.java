package org.springframework.samples.ntfh.marketcard;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketCardService {

    @Autowired
    private MarketCardRepository marketCardRepository;

    @Transactional
    public Integer marketCardCount() {
        return (int) marketCardRepository.count();
    }

    @Transactional
    public Iterable<MarketCard> findAll() {
        return marketCardRepository.findAll();
    }

    @Transactional
    public Optional<MarketCard> findMarketCardById(Integer id) {
        return marketCardRepository.findById(id);
    }

    // Only GET methods are available because all cards data will be injected from
    // the data.sql file

}
