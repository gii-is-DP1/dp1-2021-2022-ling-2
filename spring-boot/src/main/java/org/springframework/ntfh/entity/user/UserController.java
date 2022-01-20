package org.springframework.ntfh.entity.user;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.achievement.Achievement;
import org.springframework.ntfh.entity.achievement.AchievementService;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.ntfh.entity.game.GameService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrsdt
 */
@RestController()
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private AchievementService achievementService;

    @GetMapping
    public ResponseEntity<Iterable<User>> findPage(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
        List<User> users = this.userService.findPage(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("count")
    public ResponseEntity<Integer> getCount() {
        Integer userCount = this.userService.count();
        return new ResponseEntity<>(userCount, HttpStatus.OK);
    }

    /**
     * Get information about a user. Should only return non-sensitive information
     * 
     * @param username that we want to fetch from the database
     * @return User object with only non-sensitive information
     * @author andrsdt
     */
    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("userId") User user) {
        return user;
    }

    /**
     * @param user object with the data to be updated with
     * @param token jwt token of the user or the admin.
     * @return token for user's authentication, in case he/she was the one who updated the profile
     * @author andrsdt
     * 
     */
    @PutMapping
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody User user,
            @RequestHeader("Authorization") String token) {
        User updatedUser = userService.updateUser(user, token);
        Boolean sentByAdmin = TokenUtils.tokenHasAnyAuthorities(token, "admin");
        Boolean editingOwnProfile = TokenUtils.usernameFromToken(token).equals(user.getUsername());
        Map<String, String> returnBody = null;

        // Don't return a new token if an admin is editing another user's profile
        if (updatedUser != null && (!sentByAdmin || editingOwnProfile)) {
            String tokenWithUpdatedData = TokenUtils.generateJWTToken(updatedUser);
            returnBody = Map.of("authorization", tokenWithUpdatedData);
        }
        return new ResponseEntity<>(returnBody, HttpStatus.OK);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    // TODO 🔼 do this in the rest of controllers that return no body
    public void register(@RequestBody User user) {
        this.userService.createUser(user);
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        String token = userService.loginUser(user);
        return new ResponseEntity<>(Map.of("authorization", token), HttpStatus.OK);
    }

    @PutMapping("{userId}/ban")
    @ResponseStatus(HttpStatus.OK)
    public void toggleBanUser(@PathVariable("userId") String username, @RequestHeader("Authorization") String token) {
        userService.toggleBanUser(username, token);
    }

    @GetMapping("{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Game> getfindByUser(@PathVariable("userId") User user) {
        return gameService.findFinishedByUser(user);
    }

    @GetMapping("{userId}/achievements")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Achievement> achievements(@PathVariable("userId") User user) {
        return achievementService.findByUser(user);
    }

    /**
     * @author alegestor
     */
    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("userId") User user, @RequestHeader("Authorization") String token) {
        userService.deleteUser(user);
    }

}
