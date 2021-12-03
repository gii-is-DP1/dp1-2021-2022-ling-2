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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.ntfh.character.Character;
import org.springframework.samples.ntfh.exceptions.BannedUserException;
import org.springframework.samples.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.samples.ntfh.user.authorities.AuthoritiesService;
import org.springframework.samples.ntfh.util.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andrsdt
 */
@Service
public class UserService {

	private UserRepository userRepository;
	private AuthoritiesService authoritiesService;

	@Autowired
	public UserService(UserRepository userRepository, AuthoritiesService authoritiesService) {
		// TODO can the constructor ve removed and annotate attributes with 2
		// autowireds? I think so
		this.userRepository = userRepository;
		this.authoritiesService = authoritiesService;
	}

	/**
	 * Create a new user
	 * 
	 * @param user
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional
	public User saveUser(User user) throws DataIntegrityViolationException, IllegalArgumentException {

		Optional<User> userWithSameUsername = userRepository.findById(user.getUsername());
		if (userWithSameUsername.isPresent())
			throw new DataIntegrityViolationException("This username is already in use") {
			};

		Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
		if (userWithSameEmail.isPresent())
			throw new DataIntegrityViolationException("This email is already in use") {
			};

		if (user.getUsername().isEmpty())
			throw new IllegalArgumentException("The username can not be empty") {
			};
		if (user.getPassword().isEmpty())
			throw new IllegalArgumentException("The password can not be empty") {
			};
		if (user.getEmail().isEmpty())
			throw new IllegalArgumentException("The email can not be empty") {
			};

		if (user.getPassword() == null || user.getPassword().isEmpty())
			throw new IllegalArgumentException("A Password is required") {
			};

		if (user.getPassword().length() < 4)
			throw new IllegalArgumentException("Password must be at least 8 characters long") {
			};

		if (user.getUsername().length() < 4)
			throw new IllegalArgumentException("Username must be at least 4 characters long") {
			};

		if (user.getUsername().length() > 20)
			throw new IllegalArgumentException("Username must be at most 20 characters long") {
			};

		user.setEnabled(true);
		userRepository.save(user);
		authoritiesService.saveAuthorities(user.getUsername(), "user");
		return user;
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<User> findUser(String username) {
		// The username is the id (primary key)
		return userRepository.findById(username);
	}

	/**
	 * This method is used to find an user but only return non-sensitive information
	 * (username, email, authorities)
	 * 
	 * @param username the username of the user to find
	 * @return the user with restricted information
	 * @author andrsdt
	 */
	@Transactional(readOnly = true)
	public Map<String, String> findUserPublic(String username) {
		// TODO: Delete this method and create a custom JSON builder that receives
		// a User and returns only non-sensitive information
		// The username is the id (primary key)
		Optional<User> userOptional = userRepository.findById(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			HashMap<String, String> res = new HashMap<>();
			res.put("username", user.getUsername());
			res.put("email", user.getEmail());
			return res;
		} else
			return Map.of();
	}

	/**
	 * Update a user's information
	 * 
	 * @param user
	 * @return
	 * @throws DataAccessException
	 * @throws NonMatchingTokenException
	 * @throws DataIntegrityViolationException
	 * @author andrsdt
	 */
	@Transactional
	public User updateUser(User user, String token) throws DataAccessException, DataIntegrityViolationException,
			NonMatchingTokenException, IllegalArgumentException {

		if (user.getUsername().isEmpty())
			throw new IllegalArgumentException("The username cannot be empty") {
			};
		if (user.getEmail().isEmpty())
			throw new IllegalArgumentException("The email cannot be empty") {
			};

		Boolean sentByAdmin = TokenUtils.tokenHasAnyAuthorities(token, "admin");
		Boolean sentBySameUser = TokenUtils.usernameFromToken(token).equals(user.getUsername());
		if (!sentBySameUser && !sentByAdmin)
			throw new NonMatchingTokenException("A user's profile can only be updated by him/herself or by an admin");

		Optional<User> userInDatabaseOptional = this.findUser(user.getUsername());
		if (!userInDatabaseOptional.isPresent())
			throw new DataAccessException("User not found") {
			};

		User userInDatabase = userInDatabaseOptional.get();

		Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
		if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getUsername().equals(user.getUsername())) {
			throw new DataIntegrityViolationException("This email is already in use") {
			};
		}
		if (user.getPassword() != null && user.getPassword().length() < 4)
			throw new IllegalArgumentException("Password must be at least 4 characters long") {
			};

		// Before updating, make sure there are no null values. If the user didn't send
		// them in the form, they must stay the same as they were in the database.
		if (user.getPassword() == null || user.getPassword().equals("null"))
			user.setPassword(userInDatabase.getPassword());
		if (user.getEmail() == null)
			user.setEmail(userInDatabase.getEmail());
		if (user.getEnabled() == null) {
			user.setEnabled(userInDatabase.getEnabled());
		}
		return userRepository.save(user);
	}

	@Transactional
	public String loginUser(User user) throws DataAccessException, IllegalArgumentException, BannedUserException {
		Optional<User> foundUserOptional = userRepository.findById(user.getUsername());
		if (!foundUserOptional.isPresent()) {
			throw new DataAccessException("User not found") {
			};
		}
		User userInDB = foundUserOptional.get();
		if (!userInDB.getEnabled()) {
			throw new BannedUserException("This user has been banned") {
			};
		}
		if (!userInDB.getPassword().equals(user.getPassword())) {
			throw new IllegalArgumentException("Incorrect password") {
			};
		}
		return TokenUtils.generateJWTToken(userInDB);
	}

	@Transactional
	public User setCharacter(String username, Character character) throws DataAccessException {
		Optional<User> foundUserOptional = userRepository.findById(username);
		if (!foundUserOptional.isPresent()) {
			throw new DataAccessException("User not found") {
			};
		}
		User user = foundUserOptional.get();
		user.setCharacter(character);
		return user;

	}

	@Transactional
	public User banUser(User user) throws DataAccessException {
		Optional<User> foundUserOptional = userRepository.findById(user.getUsername());
		if (!foundUserOptional.isPresent()) {
			throw new DataAccessException("User not found") {
			};
		}
		User userInDB = foundUserOptional.get();
		userInDB.setEnabled(user.getEnabled());
		return user;
	}

	@Transactional
	public void deleteUser(User user) {
		this.userRepository.deleteById(user.getUsername());
	}

}
