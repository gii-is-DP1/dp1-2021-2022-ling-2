package org.springframework.ntfh.entity.scene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scenes")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @GetMapping("count")
    public ResponseEntity<Integer> getCount() {
        Integer sceneCount = sceneService.count();
        return new ResponseEntity<>(sceneCount, HttpStatus.OK);
    }
}
