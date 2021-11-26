package org.springframework.samples.ntfh.player;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.ntfh.lobby.Lobby;
import org.springframework.samples.ntfh.user.User;
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
    public Optional<Player> findPlayer(Integer id) {
        return playerRepository.findById(id);
    }

    /**
     * Creates a new player with the given user information and lobby that the
     * player will be created from.
     * 
     * @param user
     * @param lobby
     * @return
     */
    @Transactional
    public Player createFromUser(User user, Lobby lobby) {
        // TODO untested
        Player player = new Player();
        player.setGlory(0);
        player.setKills(0);
        player.setGold(0);
        player.setWounds(0);
        // TODO exception if there is no user to associate with the player (should
        // this happen?)
        player.setUser(user);
        // TODO exception if the user hasn't selected a character in the lobby (should
        // have)
        player.setCharacterType(user.getCharacter());
        return playerRepository.save(player);
    }
}
