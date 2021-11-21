package org.springframework.samples.ntfh.lobby;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.ntfh.user.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

/**
 * @author andrsdt
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lobbies")
public class LobbyController {
    @Autowired
    private LobbyService lobbyService;

    /**
     * This endpoint handles the creation of a new game lobby
     * 
     * @param game data introduced by the game creator in the form
     * @return id of the game so the user can be redirected to the lobby
     * @author andrsdt
     */
    @PostMapping()
    public ResponseEntity<Map<String, Integer>> createLobby(@RequestBody Lobby lobby) {
        lobby.setHasStarted(false); // A new lobby has not started yet as a game
        Lobby createdLobby = lobbyService.save(lobby);
        return new ResponseEntity<>(Map.of("lobbyId", createdLobby.getId()), HttpStatus.CREATED);
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
        // TODO untested
        Optional<Lobby> lobbyOptional = lobbyService.findLobbyById(lobbyId);
        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            // TODO currently returns user's password info. Maybe create a custom JSON
            // parser to hide sensitive info?
            return new ResponseEntity<>(lobby, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This endpoint will be only accessible for the host of the game (via JWT token
     * validation) and will allow him/her to kick players, start the game..
     * 
     * @author andrsdt
     */
    @PutMapping("{lobbyId}")
    public ResponseEntity<Lobby> updateLobby(@PathVariable("lobbyId") Integer lobbyId, @RequestBody Lobby lobby,
            @RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.replace("Bearer ", "");
        JwtParser jwtParser = Jwts.parser();
        String user = (String) jwtParser.parse(tokenWithoutBearer).getBody(); // .get("user", User.class);
        System.out.println(user); // TODO validate token or is it done automatically by Spring? I think so
        User authUser = null; // TODO

        if (!authUser.getUsername().equals(lobby.getHost())) // Only the host can modify the game lobby
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        // return new ResponseEntity<>(gameService.updateLobby(gameId, game),
        // HttpStatus.OK);
        return null;
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
    @PostMapping("{lobbyId}/join")
    public ResponseEntity<Lobby> joinLobby(@PathVariable("lobbyId") Integer lobbyId,
            @RequestBody Map<String, String> body, @RequestHeader("Authorization") String token) {
        // TODO check authentication before. After that, make sure that "username"
        // coincides with token's username.
        String username = body.get("username");
        Optional<Lobby> lobbyOptional = lobbyService.findLobbyById(lobbyId);
        if (!lobbyOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Lobby lobby = lobbyOptional.get();
        if (Boolean.TRUE.equals(lobby.getHasStarted()) || lobby.getUsers().size() == lobby.getMaxPlayers())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (Boolean.TRUE.equals(lobbyService.joinLobby(lobbyId, username))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
