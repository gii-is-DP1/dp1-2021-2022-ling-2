package org.springframework.samples.ntfh.playablecard;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayableCardService {

    @Autowired
    private PlayableCardRepository cardRepository;

    @Transactional
    public Integer cardCount() {
        return (int) cardRepository.count();
    }

    @Transactional
    public Iterable<PlayableCard> findAll() {
        return cardRepository.findAll();
    }

    @Transactional
    public Optional<PlayableCard> findSceneById(Integer id) {
        return cardRepository.findById(id);
    }

    // Only GET methods are available because all cards data will be injected from
    // the data.sql file

}
