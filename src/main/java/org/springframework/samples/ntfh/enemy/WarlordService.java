package org.springframework.samples.ntfh.enemy;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarlordService {
    @Autowired
    private WarlordRepository warlordRepository;

    @Transactional
    public Iterable<Warlord> findAll() {
        return warlordRepository.findAll();
    }

    @Transactional
    public Optional<Warlord> findWarlordById(Integer id) {
        return warlordRepository.findById(id);
    }

    // Only GET methods are available because all Warlord data will be injected from
    // the data.sql file
}
