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
package org.springframework.ntfh.entity.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.character.Character;
import org.springframework.ntfh.entity.user.authorities.AuthoritiesService;
import org.springframework.ntfh.exceptions.BannedUserException;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andrsdt
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository) {
		// TODO needed?
		this.userRepository = userRepository;
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
		if (userRepository.existsByEmail(user.getEmail()))
			throw new IllegalArgumentException("There is already a user registered with the email provided");

		if (userRepository.existsByUsername(user.getUsername()))
			throw new IllegalArgumentException("There is already a user registered with the username provided");

		// encrypt the password using bcrypt
		String encodedParamPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedParamPassword);

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
	public Page<User> findAllPage(Pageable pageable) {
		return userRepository.findAllPage(pageable);
	}

	@Transactional(readOnly = true)
	public Optional<User> findUser(String username) {
		// The username is the id (primary key)
		return userRepository.findById(username);
	}

	@Transactional(readOnly = true)
	public Integer count() {
		return (int) userRepository.count();
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

		// Before updating, make sure there are no null values. If the user didn't send
		// them in the form, they must stay the same as they were in the database.
		if (user.getPassword() == null || user.getPassword().equals("null")) {
			user.setPassword(userInDatabase.getPassword());
		} else {
			// If there is a new password input, encrypt it using bcrypt
			String encodedParamPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedParamPassword);
		}
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
			// TODO move this validation to the findUser method in the service?
			throw new DataAccessException("User not found") {
			};
		}

		User userInDB = foundUserOptional.get();
		if (!userInDB.getEnabled()) {
			throw new BannedUserException("This user has been banned") {
			};
		}

		if (!passwordEncoder.matches(user.getPassword(), userInDB.getPassword())) {
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
