package org.springframework.samples.ntfh.scene;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SceneService {
    @Autowired
    private SceneRepository sceneNTFHRepository;

    @Transactional
    public Integer sceneNTFHCount() {
        return (int) sceneNTFHRepository.count();
    }

    @Transactional
    public Iterable<Scene> findAll() {
        return sceneNTFHRepository.findAll();
    }

    @Transactional
    public Optional<Scene> findSceneById(int id) {
        return sceneNTFHRepository.findById(id);
    }

    @Transactional
    public void save(Scene scene) {
        sceneNTFHRepository.save(scene);
    }

    public void delete(int sceneId) {
        sceneNTFHRepository.deleteById(sceneId);
    }
}
