package org.springframework.samples.ntfh.user.unregistered;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jstockwell
 * @author andrsdt
 */
@Service
public class UnregisteredUserService {

    private UnregisteredUserRepository unregisteredUserRepository;

    @Autowired
    public UnregisteredUserService(UnregisteredUserRepository unregisteredUserRepository) {
        this.unregisteredUserRepository = unregisteredUserRepository;
    }

    @Transactional
    public void deleteUnregisteredUser(UnregisteredUser unregisteredUser) {
        unregisteredUserRepository.delete(unregisteredUser);
    }

    @Transactional
    public void saveUnregisteredUser(UnregisteredUser unregisteredUser) {
        unregisteredUserRepository.save(unregisteredUser);
    }
}
