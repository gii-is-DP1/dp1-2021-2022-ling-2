package org.springframework.ntfh.entity.round;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoundService {

    @Autowired
    private RoundRepository roundRepository;

    @Transactional
    public Integer roundCount() {
        return (int) roundRepository.count();
    }

    @Transactional
    public Iterable<Round> findAll() {
        return roundRepository.findAll();
    }

    @Transactional
    public Optional<Round> findRoundById(Integer id) {
        return roundRepository.findById(id);
    }

    @Transactional
    public void save(Round round) {
        roundRepository.save(round);
    }

    public void delete(int roundId) {
        roundRepository.deleteById(roundId);
    }
}
