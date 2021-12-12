package org.springframework.ntfh.entity.lobby;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.ntfh.exceptions.MissingAttributeException;
import org.springframework.ntfh.exceptions.UserAlreadyInLobbyException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;

@Service
public class LobbyService {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Integer lobbyCount() {
        return (int) lobbyRepository.count();
    }

    @Transactional
    public Iterable<Lobby> findAll() {
        return this.lobbyRepository.findAll();
    }

    @Transactional
    public Optional<Lobby> findLobbyById(int id) {
        return lobbyRepository.findById(id);
    }

    @Transactional
    public Lobby findLobby(Integer lobbyId) {
        return lobbyRepository.findById(lobbyId).orElseThrow(() -> new DataAccessException("Lobby not found") {
        });
    }

    @Transactional
    public Lobby save(@Valid Lobby lobby) {
        return this.lobbyRepository.save(lobby);
    }

    @Transactional
    public void deleteLobby(Lobby lobby) {
        // make sure to remove all FK refrences to this lobby from the users who were in
        // the lobby
        if (lobby.getGame() != null)
            throw new DataAccessException("The game has already started") {
            };

        lobby.getUsers().forEach(user -> {
            user.setLobby(null);
            user.setCharacter(null);
        });
        this.lobbyRepository.deleteById(lobby.getId());
    }

    /**
     * Adds the given player to the list of players in the lobby.
     * 
     * @author andrsdt
     * @param lobbyId
     * @param usernameFromRequest username that will be added to the lobby
     * @param token               JWT token sent by the client
     * @return true if the player was added, false if there was some problem
     */
    @Transactional
    public Lobby joinLobby(Integer lobbyId, String usernameFromRequest, String token)
            throws DataAccessException, MaximumLobbyCapacityException {
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(lobbyId);
        if (!lobbyOptional.isPresent())
            throw new DataAccessException("The lobby does not exist") {
            };

        Lobby lobby = lobbyOptional.get();
        if (lobby.getMaxPlayers().equals(lobby.getUsers().size()))
            throw new MaximumLobbyCapacityException("The lobby is full") {
            };

        Optional<User> userOptional = userService.findUser(usernameFromRequest);
        if (!userOptional.isPresent())
            throw new DataAccessException("The user who wants to join the lobby does not exist") {
            };

        String usernameFromToken = TokenUtils.usernameFromToken(token);
        if (!usernameFromRequest.equals(usernameFromToken))
            throw new IllegalArgumentException("The Token username and the request one does not coindice") {
            };

        Boolean userInLobby = lobby.getUsers().stream().anyMatch(u -> u.getUsername().equals(usernameFromRequest));
        if (userInLobby)
            throw new IllegalArgumentException("The user is already in the lobby") {
            };

        User user = userOptional.get();
        if (user.getLobby() != null)
            throw new UserAlreadyInLobbyException(
                    String.format("The user is already in lobby \"%s\"", user.getLobby().getName())) {
            };

        user.setLobby(lobby);
        user.setCharacter(null);
        lobby.addUser(user);
        return lobbyRepository.save(lobby);
    }

    /**
     * Removes the given player from the list of players in the lobby. Either
     * because he/she left or was kicked by the host
     * 
     * @author andrsdt
     * @param lobbyId
     * @param username
     * @return true if the player was removed, false if there was some problem
     */
    @Transactional
    public Boolean removeUserFromLobby(Lobby lobby, String username) throws DataAccessException {
        Optional<User> userOptional = userService.findUser(username);
        if (!userOptional.isPresent())
            throw new DataAccessException("The user that is being removed from the lobby does not exist") {
            };

        User user = userOptional.get();

        if (lobby.getHost().getUsername().equals(user.getUsername()))
            // this should be handled by .deleteLobby()
            throw new DataAccessException("The host cannot leave the lobby") {
            };

        lobby.removeUser(user);
        user.setLobby(null);

        this.updateLobby(lobby);
        return true;
    }

    /**
     * @author andrsdt
     * @param lobby
     * @return
     */
    @Transactional
    public Lobby updateLobby(Lobby lobby) throws MissingAttributeException {
        return this.lobbyRepository.save(lobby);
    }
}
