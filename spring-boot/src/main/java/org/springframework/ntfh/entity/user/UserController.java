package org.springframework.ntfh.entity.user;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.character.CharacterService;
import org.springframework.ntfh.entity.game.Game;
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
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/users")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private CharacterService characterService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Iterable<User> findPage(@PageableDefault(page = 0, size = 10) final Pageable pageable) {
		List<User> users = this.userService.findPage(pageable);
		return users;
	}

	@GetMapping("count")
	@ResponseStatus(HttpStatus.OK)
	public Integer getCount() {
		Integer userCount = this.userService.count();
		return userCount;
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
	public User getUser(@PathVariable("userId") String username) {
		User user = this.userService.findUser(username);
		return user;
	}

	/**
	 * <<<<<<< HEAD Update The profile of a user. Check before if the authorization token is either from the exact user
	 * or from any admin. ======= Update The profile of a user. Check before if the authorization token is either from
	 * the exact user or from any admin. >>>>>>> origin/master
	 * 
	 * @param user object with the data to be updated with
	 * @param token jwt token of the user or the admin.
	 * @return token for user's authentication, in case he/she was the one who updated the profile
	 * @author andrsdt
	 * 
	 */
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> updateUser(@RequestBody User user,
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
		return returnBody;
	}

	@PostMapping("register")
	@ResponseStatus(HttpStatus.CREATED)
	// TODO 🔼 do this in the rest of controllers that return no body
	public void register(@RequestBody User user) {
		this.userService.createUser(user);
	}


	// ! TODO este se comporta de manera distinta parece

	@PostMapping("login")
	public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
		String token = userService.loginUser(user);
		return new ResponseEntity<>(Map.of("authorization", token), HttpStatus.OK);
	}

	@PutMapping("{userId}/character/{characterId}")
	@ResponseStatus(HttpStatus.OK)
	public void setCharacter(@PathVariable("userId") String userId, @PathVariable("characterId") Integer characterId,
			@RequestHeader("Authorization") String token) {
		// TODO use converters for this
		User user = this.userService.findUser(userId);
		Character character = this.characterService.findById(characterId);
		userService.setCharacter(user.getUsername(), character);
	}

	@PutMapping("{userId}/ban")
	@ResponseStatus(HttpStatus.OK)
	public void toggleBanUser(@PathVariable("userId") String username, @RequestHeader("Authorization") String token) {
		userService.toggleBanUser(username, token);
	}

	// TODO implement
	// ! TODO no le gusta la lista vacía de momento, requiere que sea implementado
	
	@GetMapping("{userId}/history")
	public ResponseEntity<Iterable<Game>> getfindByUser(@PathVariable("userId") String username) {
		// Iterable<GameHistory> gameHistory =
		// gameHistoryService.findByGamePlayersContaining(username);
		return new ResponseEntity<>(List.of(), HttpStatus.OK);
	}

	/**
	 * @author alegestor
	 */
	@DeleteMapping("{userId}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("userId") String username, @RequestHeader("Authorization") String token) {
		// TODO better parse username to user with a converter? saw that in the slides
		User user = userService.findUser(username);
		userService.deleteUser(user);
	}

}
