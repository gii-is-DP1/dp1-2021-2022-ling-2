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

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
			String token = generateJWTToken(foundUser);
			return new ResponseEntity<>(Map.of("authorization", token), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	String generateJWTToken(User user) {
		return Jwts.builder().setSubject(user.getUsername()).claim("authorities", user.getAuthorities())
				.setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "NoTimeForHeroesSecretKey").compact();
	}
}
