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
import org.springframework.samples.ntfh.user.authorities.AuthoritiesService;
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
		this.userRepository = userRepository;
		this.authoritiesService = authoritiesService;
	}

	@Transactional
	public User saveUser(User user) throws DataAccessException {

		Optional<User> userWithSameUsername = userRepository.findById(user.getUsername());
		if (userWithSameUsername.isPresent()) {
			throw new DataAccessException("This username is already in use") {
			};
		}
		Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
		if (userWithSameEmail.isPresent()) {
			throw new DataAccessException("This email is already in use") {
			};
		} else

		{
			user.setEnabled(true);
			userRepository.save(user);
			authoritiesService.saveAuthorities(user.getUsername(), "user");
			return user;
		}
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
	 * @author andrsdt
	 * @param username the username of the user to find
	 * @return the user with restricted information
	 */
	@Transactional(readOnly = true)
	public Map<String, String> findUserPublic(String username) {
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
}
