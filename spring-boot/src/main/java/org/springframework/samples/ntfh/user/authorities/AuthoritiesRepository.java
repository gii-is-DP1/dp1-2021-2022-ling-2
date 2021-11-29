package org.springframework.samples.ntfh.user.authorities;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.ntfh.user.User;

public interface AuthoritiesRepository extends CrudRepository<Authorities, String> {
    Set<Authorities> findByUser(User user);
}
