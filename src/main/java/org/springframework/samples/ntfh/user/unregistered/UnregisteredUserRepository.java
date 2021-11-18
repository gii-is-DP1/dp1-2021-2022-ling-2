package org.springframework.samples.ntfh.user.unregistered;

import org.springframework.data.repository.CrudRepository;

/**
 * @author jstockwell
 */
public interface UnregisteredUserRepository extends CrudRepository<UnregisteredUser, String> {

}
