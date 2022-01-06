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

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ntfh.entity.character.Character;
import org.springframework.ntfh.entity.user.authorities.AuthoritiesService;
import org.springframework.ntfh.exceptions.BannedUserException;
import org.springframework.ntfh.exceptions.NonMatchingTokenException;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * @author andrsdt
 */
@Slf4j
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
	public User createUser(User user) throws DataIntegrityViolationException, IllegalArgumentException {
		if (userRepository.existsByEmail(user.getEmail()))
			throw new IllegalArgumentException("There is already a user registered with the email provided");

		if (userRepository.existsByUsername(user.getUsername()))
			throw new IllegalArgumentException("There is already a user registered with the username provided");

		// encrypt the password using bcrypt
		String encodedParamPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedParamPassword);

		user.setEnabled(true);
		this.save(user);
		authoritiesService.saveAuthorities(user.getUsername(), "user");
		log.info("User " + user.getUsername() + " created");
		return user;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<User> findPage(Pageable pageable) {
		Page<User> page = userRepository.findAllPage(pageable);
		return page.getContent();
	}

	@Transactional(readOnly = true)
	public User findUser(String username) throws DataAccessException {
		// The username is the id (primary key)
		Optional<User> user = userRepository.findById(username);
		if (!user.isPresent())
			throw new DataAccessException("User " + username + " was not found") {
			};
		return user.get();
	}

	@Transactional(readOnly = true)
	public Integer count() {
		return (int) userRepository.count();
	}

	/**
	 * Update a user's information
	 * 
	 * @author andrsdt
	 * @param user
	 * @return
	 * @throws DataAccessException
	 * @throws NonMatchingTokenException
	 * @throws DataIntegrityViolationException
	 */
	@Transactional
	public User updateUser(User user, String token) throws DataAccessException, DataIntegrityViolationException,
			NonMatchingTokenException, IllegalArgumentException {
		Boolean sentByAdmin = TokenUtils.tokenHasAnyAuthorities(token, "admin");
		Boolean sentBySameUser = TokenUtils.usernameFromToken(token).equals(user.getUsername());
		if (!sentBySameUser && !sentByAdmin)
			throw new NonMatchingTokenException("A user's profile can only be updated by him/herself or by an admin");

		Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
		if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getUsername().equals(user.getUsername())) {
			throw new DataIntegrityViolationException("This email is already in use") {
			};
		}

		// Before updating, make sure there are no null values. If the user didn't send
		// them in the form, they must stay the same as they were in the database.

		User userInDB = this.findUser(user.getUsername());
		if (user.getEmail() != null) {
			// If there is a new email, set it on the database
			userInDB.setEmail(user.getEmail());
		}
		if (user.getPassword() != null) {
			// If there is a new password input, encrypt it using bcrypt
			String encodedParamPassword = passwordEncoder.encode(user.getPassword());
			userInDB.setPassword(encodedParamPassword);
		}
		return userInDB;
	}

	@Transactional
	public String loginUser(User user) throws DataAccessException, IllegalArgumentException, BannedUserException {
		User userInDB = this.findUser(user.getUsername());
		if (!userInDB.getEnabled()) {
			throw new BannedUserException("You have been banned") {
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
		User userInDB = this.findUser(username);
		userInDB.setCharacter(character);
		return userInDB;

	}

	@Transactional
	public User toggleBanUser(String username, String token) throws DataAccessException {
		User userInDB = this.findUser(username);
		userInDB.setEnabled(!userInDB.getEnabled());
		return userInDB;
	}

	@Transactional
	public void deleteUser(User user) {
		if (user.getLobby() != null && user.getLobby().getHasStarted()) {
			throw new IllegalStateException("You cannot delete a user while he/she is playing a game");
		}
		this.userRepository.deleteById(user.getUsername());
	}

}
