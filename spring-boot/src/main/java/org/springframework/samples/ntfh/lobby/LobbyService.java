package org.springframework.samples.ntfh.lobby;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.ntfh.exceptions.MaximumLobbyCapacityException;
import org.springframework.samples.ntfh.exceptions.MissingAttributeException;
import org.springframework.samples.ntfh.exceptions.UserAlreadyInLobbyException;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.samples.ntfh.util.TokenUtils;
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

    /**
     * 
     * @author andrsdt
     * @param id of the lobby to be fetched
     * @return Lobby object without sensitive information (users' passwords)
     */
    @Transactional
    public Optional<Lobby> findLobbyByIdNonSensitive(int id) throws DataAccessException {
        // TODO unused. Maybe replace with a custom JSON parser?
        Optional<Lobby> lobbyOptional = lobbyRepository.findById(id);
        if (!lobbyOptional.isPresent())
            throw new DataAccessException("The lobby does not exist") {
            };

        Lobby lobby = lobbyOptional.get();

        // Create a Lobby object without sensitive information
        Lobby lobbyNonSensitive = new Lobby();
        lobbyNonSensitive.setId(lobby.getId());
        lobbyNonSensitive.setName(lobby.getName());
        lobbyNonSensitive.setHasScenes(lobby.getHasScenes());
        lobbyNonSensitive.setSpectatorsAllowed(lobby.getSpectatorsAllowed());
        lobbyNonSensitive.setMaxPlayers(lobby.getMaxPlayers());
        Set<User> usersNonSensitive = new HashSet<>();
        Set<User> lobbyUsers = lobbyNonSensitive.getUsers();
        if (lobbyUsers != null) {
            lobbyUsers.forEach(user -> {
                User userNonSensitive = new User();
                userNonSensitive.setUsername(user.getUsername());
                usersNonSensitive.add(userNonSensitive);
            });
            lobbyNonSensitive.setUsers(usersNonSensitive);
        }
        return Optional.of(lobbyNonSensitive); // assure that it always returns an optional
    }

    @Transactional
    public Lobby save(@Valid Lobby lobby) {
        return this.lobbyRepository.save(lobby);
    }

    @Transactional
    public void deleteLobby(Lobby lobby) {
        // make sure to remove all FK refrences to this lobby from the users who were in
        // the lobby
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
        // TODO untested
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
        // TODO check if there are missing attributes in the object? should there be
        // any?
        if (lobby.getName().isEmpty())
            throw new MissingAttributeException("The name of the lobby cannot be empty") {
            };

        if (lobby.getHasScenes() == null)
            throw new MissingAttributeException("The scenes setting must be enable or disable") {
            };

        if (lobby.getMaxPlayers() == null)
            throw new MissingAttributeException("The number of max players can not be null") {
            };

        return this.lobbyRepository.save(lobby);
    }
}
