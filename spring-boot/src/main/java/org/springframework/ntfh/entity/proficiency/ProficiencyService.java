package org.springframework.ntfh.entity.proficiency;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProficiencyService {
    @Autowired
    private ProficiencyRepository proficiencyRepository;

    @Transactional
    public Integer count() {
        return (int) proficiencyRepository.count();
    }

    @Transactional
    public Iterable<Proficiency> findAll() {
        return proficiencyRepository.findAll();
    }

    @Transactional
    public Optional<Proficiency> findProficiencyById(Integer id) {
        return proficiencyRepository.findById(id);
    }

}
