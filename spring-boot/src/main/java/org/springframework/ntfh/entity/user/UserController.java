package org.springframework.ntfh.entity.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.game.history.GameHistory;
import org.springframework.ntfh.entity.game.history.GameHistoryRepository;
import org.springframework.ntfh.entity.user.authorities.Authorities;
import org.springframework.ntfh.entity.user.authorities.AuthoritiesService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrsdt
 */
@RestController()
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/users")
public class UserController {
	// TODO JWT tokens can be decrypted to know if the user who is trying to perform
	// an action is accessing his data or not
	@Autowired
	private UserService userService;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	private GameHistoryRepository gameHistoryRepository;

	@GetMapping
	public ResponseEntity<Iterable<User>> getAll(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
		Page<User> usersPage = this.userService.findAllPage(pageable);
		List<User> users = usersPage.getContent();
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
	public ResponseEntity<User> getUser(@PathVariable("userId") String username) {
		Optional<User> user = this.userService.findUser(username);
		if (!user.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		// TODO create a custom response that only returns the non-sensitive information
		return new ResponseEntity<>(user.get(), HttpStatus.OK);
	}

	/**
	 * Update The profile of a user. Check before if the authorization token is
	 * either from the exact user or from any admin.
	 * 
	 * @param user  object with the data to be updated with
	 * @param token jwt token of the user or the admin.
	 * @return token for user's authentication, in case he/she was the one who
	 *         updated the profile
	 * @author andrsdt
	 * 
	 */
	@PutMapping()
	public ResponseEntity<Map<String, String>> updateUser(@RequestBody User user,
			@RequestHeader("Authorization") String token) {

		if (user.getEnabled() != null) {
			userService.banUser(user);
		} else {
			userService.updateUser(user, token);
		}
		Boolean sentByAdmin = TokenUtils.tokenHasAnyAuthorities(token, "admin");
		Boolean editingAdminProfile = TokenUtils.usernameFromToken(token).equals(user.getUsername());
		if (sentByAdmin && !editingAdminProfile)
			// Don't return a new token if an admin is editing another user's profile
			return new ResponseEntity<>(HttpStatus.OK);

		Set<Authorities> authorities = authoritiesService.getAuthorities(user);
		user.setAuthorities(authorities);
		String tokenWithUpdatedData = TokenUtils.generateJWTToken(user);
		return new ResponseEntity<>(Map.of("authorization", tokenWithUpdatedData), HttpStatus.OK);
	}

	@PostMapping("register")
	public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
		this.userService.saveUser(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("login")
	public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
		String token = userService.loginUser(user);
		return new ResponseEntity<>(Map.of("authorization", token), HttpStatus.OK);
	}

	@PutMapping("character")
	public ResponseEntity<Map<String, String>> setCharacter(@RequestBody User user,
			@RequestHeader("Authorization") String token) {
		userService.setCharacter(user.getUsername(), user.getCharacter());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// TODO make this work
	// TODO use service instead of repository
	@GetMapping("{userId}/history")
	public ResponseEntity<Iterable<GameHistory>> getfindByUser(@PathVariable("userId") String username) {
		Iterable<GameHistory> gameHistory = this.gameHistoryRepository.findByGamePlayersContaining(username);
		return new ResponseEntity<>(gameHistory, HttpStatus.OK);
	}

	@DeleteMapping("{username}")
	public ResponseEntity<User> deleteUser(@PathVariable("username") String username,
			@RequestHeader("Authorization") String token) {
		// TODO implement
		return null;
	}

}
