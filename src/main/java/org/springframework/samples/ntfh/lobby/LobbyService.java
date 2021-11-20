package org.springframework.samples.ntfh.lobby;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.stereotype.Service;

/**
 * @author andrsdt
 */
@Service
public class LobbyService {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Iterable<Lobby> findAll() {
        return this.lobbyRepository.findAll();
    }

    @Transactional
    public Optional<Lobby> findLobbyById(int id) {
        return lobbyRepository.findById(id);
    }

    @Transactional
    public Lobby save(@Valid Lobby lobby) {
        return this.lobbyRepository.save(lobby);
    }

    @Transactional
    public void delete(Lobby lobby) {
        this.lobbyRepository.delete(lobby);
    }

    /**
     * Adds the given player to the list of players in the lobby.
     * 
     * @param lobbyId
     * @param username
     * @return true if the player was added, false if there was some problem
     */
    @Transactional
    public Boolean joinLobby(Integer lobbyId, String username) {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);
        if (!lobbyOptional.isPresent())
            return false;

        Lobby lobby = lobbyOptional.get();
        if (lobby.getMaxPlayers().equals(lobby.getUsers().size()))
            return false;

        Optional<User> userOptional = userService.findUser(username);
        if (!userOptional.isPresent())
            return false;
        User user = userOptional.get();
        lobby.addUser(user);
        lobbyRepository.save(lobby);
        return true;
    }

    /**
     * Removes the given player from the list of players in the lobby. Either
     * because he/she left or was kicked by the host
     * 
     * @param lobbyId
     * @param username
     * @return true if the player was removed, false if there was some problem
     */
    @Transactional
    public Boolean leaveLobby(Integer lobbyId, String username) {
        // TODO impelemnt
        return null;
    }
}
