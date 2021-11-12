package org.springframework.samples.ntfh.scene;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scenes")
public class SceneController {

    @Autowired
    private SceneService sceneNTFHService;

    @GetMapping()
    public String getAll(ModelMap modelMap) { // modelmap object contains the data that will be passed to the view
        String view = "scenes/listScenes";
        Iterable<Scene> scenes = sceneNTFHService.findAll();
        modelMap.addAttribute("scenes", scenes);
        return view;
    }

    @GetMapping(path = "/new")
    public String createScene(ModelMap modelMap) {
        String view = "scenes/editScene";
        modelMap.addAttribute("scene", new Scene()); // we add a new created scene to the context so we can edit it
        return view;
    }

    @PostMapping(path = "/save")
    public String saveScene(@Valid Scene scene, BindingResult result, ModelMap modelMap) {
        String view = "scenes/listScenes";
        if (result.hasErrors()) {
            modelMap.addAttribute("scene", scene); // scene with errors as new context to represent it again
            return "scenes/editScene"; // and we return to the form which should show the errors
        } else {
            sceneNTFHService.save(scene);
            modelMap.addAttribute("message", "Scene successfully saved");
            view = getAll(modelMap); // after saving the scene, we will udpate the view with the new data
        }
        return view;
    }

    @GetMapping(path = "/delete/{sceneId}")
    public String deleteScene(@PathVariable("sceneId") int sceneId, ModelMap modelMap) {
        String view = "scenes/listScenes";
        Optional<Scene> scene = sceneNTFHService.findSceneById(sceneId);
        if (scene.isPresent()) {
            sceneNTFHService.delete(sceneId);
            modelMap.addAttribute("message", "Scene successfully deleted");
            view = getAll(modelMap); // after saving the scene, we will udpate the view with the new data
        } else {
            modelMap.addAttribute("message", "Scene not found");
        }
        return view;
    }
}
