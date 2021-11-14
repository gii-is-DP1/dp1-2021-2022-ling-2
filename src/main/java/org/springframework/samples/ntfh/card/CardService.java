package org.springframework.samples.ntfh.card;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;

    @Transactional
    public Integer cardCount() {
        return (int) cardRepository.count();
    }

    @Transactional
    public Iterable<Card> findAll() {
        return cardRepository.findAll();
    }

    @Transactional
    public Optional<Card> findSceneById(Integer id) {
        return cardRepository.findById(id);
    }

}
