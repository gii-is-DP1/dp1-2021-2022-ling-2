package org.springframework.samples.ntfh.scene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/scenes")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @GetMapping()
    public ResponseEntity<Iterable<Scene>> getAll() {
        // TODO untested
        // We don't know if this method will be useful later
        Iterable<Scene> scenes = sceneService.findAll();
        return new ResponseEntity<>(scenes, HttpStatus.OK);
    }

}
