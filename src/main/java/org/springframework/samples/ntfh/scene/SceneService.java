package org.springframework.samples.ntfh.scene;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SceneService {
    @Autowired
    private SceneRepository sceneRepository;

    @Transactional
    public Integer sceneCount() {
        return (int) sceneRepository.count();
    }

    @Transactional
    public Iterable<Scene> findAll() {
        return sceneRepository.findAll();
    }

    @Transactional
    public Optional<Scene> findSceneById(Integer id) {
        return sceneRepository.findById(id);
    }

    @Transactional
    public void save(Scene scene) {
        sceneRepository.save(scene);
    }

    public void delete(int sceneId) {
        sceneRepository.deleteById(sceneId);
    }
}
