package org.springframework.samples.petclinic.sceneNTFH;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SceneNTFHService {
    @Autowired
    private SceneNTFHRepository sceneNTFHRepository;

    @Transactional
    public Integer sceneNTFHCount() {
        return (int) sceneNTFHRepository.count();
    }

    @Transactional
    public Iterable<SceneNTFH> findAll() {
        return sceneNTFHRepository.findAll();
    }
}
