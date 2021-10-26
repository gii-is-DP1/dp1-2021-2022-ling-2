package org.springframework.samples.petclinic.userNTFH;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.user.AuthoritiesService;
import org.springframework.stereotype.Service;

@Service
public class UserNTFHService {
    private UserNTFHRepository userNTFHRepository;	
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public UserNTFHService(UserNTFHRepository userNTFHRepository) {
		this.userNTFHRepository = userNTFHRepository;
	}	

	@Transactional(readOnly = true)
	public Collection<UserNTFH> findUserNTFHByUsername(String username) throws DataAccessException {
		return userNTFHRepository.findByUsername(username);
	}
    
	@Transactional
	public void saveUserNTFH(UserNTFH userNTFH) throws DataAccessException {
		//creating owner
		userNTFHRepository.save(userNTFH);		
		//creating authorities
		authoritiesService.saveAuthorities(userNTFH.getUsername(), "userNTFH");
	}

}
