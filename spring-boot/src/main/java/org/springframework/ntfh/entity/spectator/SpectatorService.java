package org.springframework.ntfh.entity.spectator;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SpectatorService {

    @Autowired
    private SpectatorRepository spectatorRepository;

    public Iterable<Spectator> findAll() {
        return spectatorRepository.findAll();
    }

    public Optional<Spectator> findSpectator(String id) {
        return spectatorRepository.findById(id);
    }

    @Transactional
    public void saveSpectator(Spectator spectator) throws DataAccessException {
        spectatorRepository.save(spectator);
    }
}
