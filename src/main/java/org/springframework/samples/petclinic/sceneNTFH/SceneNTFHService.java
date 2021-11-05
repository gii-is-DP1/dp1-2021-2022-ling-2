package org.springframework.samples.petclinic.sceneNTFH;

import java.util.Optional;

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

    @Transactional
    public Optional<SceneNTFH> findSceneById(int id) {
        return sceneNTFHRepository.findById(id);
    }

    @Transactional
    public void save(SceneNTFH scene) {
        sceneNTFHRepository.save(scene);
    }

    public void delete(int sceneId) {
        sceneNTFHRepository.deleteById(sceneId);
    }
}
