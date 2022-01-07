package org.springframework.ntfh.entity.user.unregistered;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jstockwell
 * @author andrsdt
 */
@Slf4j
@Service
public class UnregisteredUserService {

    private UnregisteredUserRepository unregisteredUserRepository;

    @Autowired
    public UnregisteredUserService(UnregisteredUserRepository unregisteredUserRepository) {
        this.unregisteredUserRepository = unregisteredUserRepository;
    }

    @Transactional(readOnly = true)
	public Iterable<UnregisteredUser> findAll() {
		return unregisteredUserRepository.findAll();
	}

    @Transactional(readOnly = true)
    public Optional<UnregisteredUser> findUnregisteredUserById(String username) {
        return this.unregisteredUserRepository.findById(username);
    }

    @Transactional
    public void delete(UnregisteredUser unregisteredUser) {
        unregisteredUserRepository.delete(unregisteredUser);
        log.info("Unregistered user " + unregisteredUser.getUsername() + " deleted");
    }

    @Transactional
    public UnregisteredUser create() {
        UnregisteredUser unregisteredUser = new UnregisteredUser();

        // generate a random username
        String username;
        do {
            Integer randomFourDigits = (int) (Math.random() * 10000);
            username = String.format("user%04d", randomFourDigits);
        } while (this.unregisteredUserRepository.findById(username).isPresent());

        // Get current time with millisecond precision (token)
        Long creationTime = System.currentTimeMillis();

        unregisteredUser.setUsername(username);
        unregisteredUser.setCreationTime(creationTime);
        unregisteredUserRepository.save(unregisteredUser);
        log.info("Unregistered user " + unregisteredUser.getUsername() + " created");
        return unregisteredUser; // so the users can store the info on their LocalStorage
    }
}
