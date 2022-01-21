package org.springframework.ntfh.entity.scene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scenes")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @GetMapping("count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCount() {
        return sceneService.count();
    }
}
