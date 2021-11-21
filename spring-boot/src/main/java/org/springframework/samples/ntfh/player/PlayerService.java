package org.springframework.samples.ntfh.player;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public void savePlayer(Player player) throws DataAccessException {
        playerRepository.save(player);
    }

    @Transactional
	public Iterable<Player> findAll() {
		return playerRepository.findAll();
	}


    @Transactional
    public Optional<Player> findPlayer(String id) {
        return playerRepository.findById(id);
    }
}
