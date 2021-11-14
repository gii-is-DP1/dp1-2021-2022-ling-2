package org.springframework.samples.ntfh.playableCard;

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

}
