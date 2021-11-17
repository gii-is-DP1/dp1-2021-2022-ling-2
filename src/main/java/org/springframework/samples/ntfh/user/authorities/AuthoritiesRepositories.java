package org.springframework.samples.ntfh.user.authorities;

import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepositories extends CrudRepository<Authorities, String> {
    
}
