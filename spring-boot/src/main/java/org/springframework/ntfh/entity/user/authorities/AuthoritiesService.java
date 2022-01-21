package org.springframework.ntfh.entity.user.authorities;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthoritiesService {

    private AuthoritiesRepository authoritiesRepository;
    private UserService userService;

    @Autowired
    public AuthoritiesService(AuthoritiesRepository authoritiesRepository, UserService userService) {
        this.authoritiesRepository = authoritiesRepository;
        this.userService = userService;
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
        User user = userService.findByUsername(username);

        authority.setUser(user);
        authority.setAuthority(role);
        authoritiesRepository.save(authority);
        log.info("Authority " + role + " added to user " + username);
    }
}
