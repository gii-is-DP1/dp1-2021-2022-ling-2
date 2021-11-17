package org.springframework.samples.ntfh.user.unregistered;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnregisteredService {

    private UnregisteredRepository unregisteredRepository;

    @Autowired
    public UnregisteredService(UnregisteredRepository unregisteredRepository) {
        this.unregisteredRepository = unregisteredRepository;
    }

    @Transactional
    public void deleteUnregistered(Unregistered unregistered) {
        unregisteredRepository.delete(unregistered);
    }

    @Transactional
    public void saveUnregistered(Unregistered unregistered) {
        unregisteredRepository.save(unregistered);
    }

    @Transactional
    public Iterable<Unregistered> findAll() {
        return unregisteredRepository.findAll();
    }

    @Transactional
    public Optional<Unregistered> findUnregistered(String id) {
        return unregisteredRepository.findById(id);
    }
}
