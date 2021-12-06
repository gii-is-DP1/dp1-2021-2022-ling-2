package org.springframework.ntfh.entity.scene;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SceneService {
    @Autowired
    private SceneRepository sceneRepository;

    @Transactional
    public Integer count() {
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

}
