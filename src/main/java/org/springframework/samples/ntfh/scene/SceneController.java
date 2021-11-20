package org.springframework.samples.ntfh.scene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scenes")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @GetMapping()
    public String getAll(ModelMap modelMap) { // modelmap object contains the data that will be passed to the view
        String view = "scenes/listScenes";
        Iterable<Scene> scenes = sceneService.findAll();
        modelMap.addAttribute("scenes", scenes);
        return view;
    }
}
