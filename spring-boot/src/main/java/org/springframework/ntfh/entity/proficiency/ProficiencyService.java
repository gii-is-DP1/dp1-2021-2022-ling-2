package org.springframework.ntfh.entity.proficiency;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProficiencyService {
    @Autowired
    private ProficiencyRepository proficiencyRepository;

    public Integer count() {
        return (int) proficiencyRepository.count();
    }

    public Iterable<Proficiency> findAll() {
        return proficiencyRepository.findAll();
    }

    public Optional<Proficiency> findProficiencyById(Integer id) {
        return proficiencyRepository.findById(id);
    }

}
