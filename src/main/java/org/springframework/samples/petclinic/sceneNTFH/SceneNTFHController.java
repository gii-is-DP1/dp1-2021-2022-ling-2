package org.springframework.samples.petclinic.sceneNTFH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scenes")
public class SceneNTFHController {

    @Autowired
    private SceneNTFHService sceneNTFHService;

    @GetMapping()
    public Iterable<SceneNTFH> getAll(ModelMap modelMap) {
        Iterable<SceneNTFH> scenes = sceneNTFHService.findAll();
        modelMap.addAttribute("scenes", scenes);
        return scenes; // Do we actually need a view for this?
    }
}
