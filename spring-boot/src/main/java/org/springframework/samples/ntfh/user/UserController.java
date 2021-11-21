/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.ntfh.user;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.ntfh.util.TokenUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<Iterable<User>> getAll() {
		// untested
		Iterable<User> users = this.userService.findAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
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

		Boolean sentByAdmin = TokenUtils.tokenHasAuthorities(token, "admin");
		Boolean sentBySameUser = TokenUtils.usernameFromToken(token).equals(user.getUsername());
		// TODO untested
		// If the token is not from the user nor an admin, return unauthorized
		if (!sentBySameUser && !sentByAdmin)
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		Optional<User> userInDatabaseOptional = this.userService.findUser(user.getUsername());
		if (!userInDatabaseOptional.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		User userInDatabase = userInDatabaseOptional.get();

		// Make sure there are no null values. If the user didn't send them in the form,
		// they must stay the same as they were in the database.
		if (user.getPassword() == null)
			user.setPassword(userInDatabase.getPassword());
		if (user.getEmail() == null)
			user.setEmail(userInDatabase.getEmail());

		userService.updateUser(user);

		if (sentByAdmin)
			// Don't return a new token if the one updating the profile is an admin
			return new ResponseEntity<>(HttpStatus.OK);

		String newToken = TokenUtils.generateJWTToken(user);
		return new ResponseEntity<>(Map.of("authorization", newToken), HttpStatus.OK);
	}

	@PostMapping("register")
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody User user) {
		this.userService.saveUser(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("login")
	public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
		Optional<User> foundUserOptional = this.userService.findUser(user.getUsername());
		if (!foundUserOptional.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else if (foundUserOptional.get().getPassword().equals(user.getPassword())) {
			User foundUser = foundUserOptional.get();
			String token = TokenUtils.generateJWTToken(foundUser);
			return new ResponseEntity<>(Map.of("authorization", token), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}
