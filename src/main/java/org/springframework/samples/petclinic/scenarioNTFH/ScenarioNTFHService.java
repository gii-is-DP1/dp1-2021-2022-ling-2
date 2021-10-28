package org.springframework.samples.petclinic.scenarioNTFH;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioNTFHService {
    @Autowired
    private ScenarioNTFHRepository scenarioNTFHRepository;

    @Transactional
    public Integer scenarioNTFHCount(){
        return (int) scenarioNTFHRepository.count();
    }

    @Transactional
    public Iterable<ScenarioNTFH> findAll(){
        return scenarioNTFHRepository.findAll();
    }
}
