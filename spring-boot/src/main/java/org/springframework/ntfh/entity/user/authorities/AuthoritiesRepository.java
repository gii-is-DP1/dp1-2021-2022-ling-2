package org.springframework.ntfh.entity.user.authorities;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.ntfh.entity.user.User;

public interface AuthoritiesRepository extends CrudRepository<Authorities, String> {
    Set<Authorities> findByUser(User user);
}
