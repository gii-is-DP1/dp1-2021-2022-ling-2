package org.springframework.samples.ntfh.user.authorities;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.ntfh.user.User;
import org.springframework.samples.ntfh.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {
    
    private AuthoritiesRepositories authoritiesRepositories;
    private UserService userService;

    @Autowired
    public AuthoritiesService(AuthoritiesRepositories authoritiesRepositories, UserService userService) {
        this.authoritiesRepositories = authoritiesRepositories;
        this.userService = userService;
    }

    @Transactional
    public void saveAuthorities(Authorities authorities) throws DataAccessException {
        authoritiesRepositories.save(authorities);
    }

    @Transactional
    public void setUser(User user) {
        
    }

    @Transactional
    public void saveAuthorities(String userId, String role) throws DataAccessException {
        Authorities authority = new Authorities();
        Optional<User> user = userService.findUser(userId);

        if(user.isPresent()) {
            authority.setUser(user.get());
            authority.setAuthority(role);
            authoritiesRepositories.save(authority);
        } else {
            throw new DataAccessException("User '" + user.getClass().getName() + "' not found!") {};
        }
    }
}
