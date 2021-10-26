package org.springframework.samples.petclinic.userNTFH;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UserNTFHRepository extends Repository<UserNTFH, Integer>{
    void save(UserNTFH userNTFH) throws DataAccessException;

    @Query("SELECT DISTINCT userNTFH FROM UserNTFH userNTFH WHERE userNTFH.username LIKE :username%")
	public Collection<UserNTFH> findByUsername(@Param("username") String username);
}
