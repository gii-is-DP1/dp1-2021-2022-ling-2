package org.springframework.ntfh.entity.user.authorities;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthoritiesService {

    private AuthoritiesRepository authoritiesRepository;
    private UserRepository userRepository;

    @Autowired
    public AuthoritiesService(AuthoritiesRepository authoritiesRepository, UserRepository userRepository) {
        this.authoritiesRepository = authoritiesRepository;
        this.userRepository = userRepository;
    }

    public Set<Authorities> getAuthorities(User user) {
        return this.authoritiesRepository.findByUser(user);
    }

    @Transactional
    public void saveAuthorities(Authorities authorities) throws DataAccessException {
        authoritiesRepository.save(authorities);
        log.info("Authority " + authorities.getAuthority() + " created");
    }

    @Transactional
    public void saveAuthorities(String username, String role) throws DataAccessException {
        Authorities authority = new Authorities();
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()) {
            authority.setUser(user.get());
            authority.setAuthority(role);
            authoritiesRepository.save(authority);
            log.info("Authority " + role + " added to user " + username);
        } else {
            throw new DataAccessException("User '" + user.getClass().getName() + "' not found!") {
            };
        }
    }
}
