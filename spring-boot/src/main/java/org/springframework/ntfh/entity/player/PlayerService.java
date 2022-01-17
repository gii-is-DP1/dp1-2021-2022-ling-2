package org.springframework.ntfh.entity.player;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.lobby.Lobby;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

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

    public Iterable<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findById(Integer playerId) throws DataAccessException {
        Optional<Player> player = playerRepository.findById(playerId);
        if (!player.isPresent())
            throw new DataAccessException("Player with id " + playerId + " was not found") {};
        return player.get();
    }

    /**
     * Creates a new player with the given user information and lobby that the player will be created from.
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

        // ! handle this before starting a game
        // if (user.getCharacter() == null) {
        // throw new IllegalArgumentException("User " + user.getUsername() + " has not selected a character");
        // }

        player.setUser(user);
        Player playerDB = playerRepository.save(player);

        user.setPlayer(player);
        userService.save(user);

        log.info("Player created by user " + user.getUsername() + " in lobby id " + lobby.getId());
        return playerDB;

    }
}
