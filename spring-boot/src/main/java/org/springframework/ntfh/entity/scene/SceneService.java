package org.springframework.ntfh.entity.scene;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SceneService {
    @Autowired
    private SceneRepository sceneRepository;

    public Integer count() {
        return (int) sceneRepository.count();
    }

    public Iterable<Scene> findAll() {
        return sceneRepository.findAll();
    }

    public Optional<Scene> findSceneById(Integer id) {
        return sceneRepository.findById(id);
    }

}
