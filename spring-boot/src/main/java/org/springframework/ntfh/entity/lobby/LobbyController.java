package org.springframework.ntfh.entity.lobby;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author andrsdt
 */
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lobbies")
public class LobbyController {
    @Autowired
    private LobbyService lobbyService;

    /**
     * This endpoint handles the fetch of all active lobbies
     * 
     * @return all active lobbies
     * @author jstockwell
     */
    @GetMapping
    public ResponseEntity<Iterable<Lobby>> getAll() {
        // untested
        Iterable<Lobby> lobbies = this.lobbyService.findAll();
        return new ResponseEntity<>(lobbies, HttpStatus.OK);
    }

    /**
     * This endpoint handles the creation of a new game lobby
     * 
     * @param game data introduced by the game creator in the form
     * @return id of the game so the user can be redirected to the lobby
     * @author andrsdt
     */
    @PostMapping
    public ResponseEntity<Map<String, Integer>> createLobby(@RequestBody Lobby lobby) {
        Lobby createdLobby = lobbyService.save(lobby);
        log.info("Lobby with id " + lobby.getId() + " created");
        return new ResponseEntity<>(Map.of("lobbyId", createdLobby.getId()), HttpStatus.CREATED);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount() {
        Integer count = lobbyService.lobbyCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * This endpoint will be pinged every few seconds by the client to check if
     * someone has joined the lobby or if the game has been started by the host
     * 
     * @return current lobby status
     * @author andrsdt
     */
    @GetMapping("{lobbyId}")
    public ResponseEntity<Lobby> getLobbyStatus(@PathVariable("lobbyId") Integer lobbyId) {
        Lobby lobby = lobbyService.findLobby(lobbyId);
        return new ResponseEntity<>(lobby, HttpStatus.OK);
    }

    /**
     * This endpoint will be called from the user's browser to join a game lobby. If
     * the response is 200 OK (The game hasn't started yet and there is still
     * space), then he/she will be redirected to the lobby
     * 
     * @param lobbyId id of the lobby that the user wants to join to
     * @param token   JWT token of the user, to validate server-side that the user
     *                who wants to join is the one who is logged in
     * @return 200 OK if the game hasn't started yet and there is still space
     * @return 404 NOT FOUND if the lobby doesn't exist
     * @return 403 FORBIDDEN if the game has already started, the players is already
     *         in the game or there is no room left
     * @return 401 UNAUTHORIZED if the user who sent the request is not logged or is
     *         not the one who he/she claims to be (JWT token validation)
     * @author andrsdt
     */
    @PostMapping("{lobbyId}/join") // TODO refactor to "{lobbyId}/add/{username}"
    @ResponseStatus(HttpStatus.OK)
    public void joinLobby(@PathVariable("lobbyId") Integer lobbyId,
            @RequestBody Map<String, String> body, @RequestHeader("Authorization") String token) {
        String usernameFromRequest = body.get("username");
        lobbyService.joinLobby(lobbyId, usernameFromRequest, token);
        log.info("User " + usernameFromRequest + " joined lobby with id " + lobbyId);
    }

    /**
     * 
     * @param lobbyId of the lobby want to remove the user from
     * @param body    with the username of the who we wanto to kick from the lobby
     * @param token   of the user who wants to kick someone. Must be the either the
     *                lobby host or the own user wanting to leave
     * @return 200 OK if the user was kicked from the lobby
     * @return 404 NOT FOUND if the lobby doesn't exist
     * @return 401 UNAUTHORIZED if the user is being requested to be removed from
     *         the lobby is not logged or is not neither the lobby host or the user
     *         sending the request (the request was sent by a malicious user)
     * @return 403 FORBIDDEN if the user is not in the lobby or another error
     *         happens
     * @author andrsdt
     */
    @DeleteMapping("{lobbyId}/remove/{username}")
    public ResponseEntity<Lobby> removeUserFromLobby(@PathVariable("lobbyId") Integer lobbyId,
            @PathVariable("username") String username, @RequestHeader("Authorization") String token) {

        Lobby lobby = lobbyService.findById(lobbyId);

        String usernameFromToken = TokenUtils.usernameFromToken(token);
        String usernameFromLobbyHost = lobby.getHost().getUsername();

        Boolean requestByUserLeaving = usernameFromToken.equals(username);
        Boolean requestByHost = usernameFromToken.equals(usernameFromLobbyHost);
        if (!requestByHost && !requestByUserLeaving) {
            throw new NonMatchingTokenException(
                    "User " + usernameFromToken + " unauthorized removal attempt from lobby id " + lobbyId);
        }

        // TODO divide in two methods
        if (requestByHost && usernameFromLobbyHost.equals(username)) {
            // If the host is the one who wanted to leave, then delete the lobby
            lobbyService.deleteLobby(lobby);
        }
        if (lobbyService.removeUserFromLobby(lobby, username)) {
            log.info("User " + username + " was removed from lobby id " + lobby.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 
     * @param lobbyId of the lobby that we want to delete. Will be called
     *                automatically when the host leaves the lobby.
     * @param token   of the user who wants to delete the lobby. Must be the host.
     * @return 200 OK if the lobby has been deleted
     * @return 403 FORBIDDEN if the user who sent the request is not the host of the
     *         lobby
     * @return 404 NOT FOUND if the lobby doesn't exist
     * @author andrsdt
     */
    @DeleteMapping("{lobbyId}")
    public ResponseEntity<Lobby> deleteLobby(@PathVariable("lobbyId") Integer lobbyId,
            @RequestHeader("Authorization") String token) {
        // TODO implement? currently delegated to removeUserFromLobby() when the one
        // leaving is the host. Kind of violates the single responsibility principle
        log.info("Lobby id " + lobbyId + " was deleted");
        return null;
    }

}
