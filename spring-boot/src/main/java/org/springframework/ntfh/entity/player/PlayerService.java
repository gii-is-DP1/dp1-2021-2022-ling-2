package org.springframework.ntfh.entity.player;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.user.User;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Integer playerCount() {
        return (int) playerRepository.count();
    }

    @Transactional
    public void savePlayer(Player player) throws DataAccessException {
        playerRepository.save(player);
    }

    @Transactional
    public void delete(Player player) {
        playerRepository.delete(player);
    }

    @Transactional
    public void deleteById(Integer id) {
        playerRepository.deleteById(id);
    }

    @Transactional
    public Iterable<Player> findAll() {
        return playerRepository.findAll();
    }

    @Transactional
    public Player findById(int id) throws DataAccessException {
        Optional<Player> player = playerRepository.findById(id);
        if (!player.isPresent())
            throw new DataAccessException("Player with id " + id + " was not found") {
            };
        return player.get();
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
    public Player createFromUser(User user, Lobby lobby, Integer turnOrder) {
        Player player = new Player();
        player.setGlory(0);
        player.setKills(0);
        player.setGold(0);
        player.setWounds(0);
        player.setGuard(0);
        player.setTurnOrder(turnOrder);

        // TODO exception if there is no user to associate with the player (should this
        // happen?)
        player.setUser(user);

        if (user.getCharacter() == null) {
            throw new IllegalArgumentException("User " + user.getUsername() + " has not selected a character");
        }

        player.setCharacterType(user.getCharacter());
        return playerRepository.save(player);
    }
}
