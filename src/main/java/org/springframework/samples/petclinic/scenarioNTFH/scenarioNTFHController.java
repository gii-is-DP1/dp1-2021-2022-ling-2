package org.springframework.samples.petclinic.scenarioNTFH;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scenarios")
public class scenarioNTFHController {

    @Autowired
    private ScenarioNTFHService scenarioNTFHService;

    @GetMapping()
    public String listaScenarios(ModelMap modelMap){
        String vista = "scenarios/listaScenarios";
        Collection<scenarioNTFH> scenarios = 
    }
}
