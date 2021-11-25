package org.springframework.samples.ntfh.spectator;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SpectatorService {

    @Autowired
    private SpectatorRepository spectatorRepository;

    @Transactional
    public void saveSpectator(Spectator spectator) throws DataAccessException {
        spectatorRepository.save(spectator);
    }

    @Transactional
    public Iterable<Spectator> findAll() {
        return spectatorRepository.findAll();
    }

    @Transactional
    public Optional<Spectator> findSpectator(String id) {
        return spectatorRepository.findById(id);
    }
}
