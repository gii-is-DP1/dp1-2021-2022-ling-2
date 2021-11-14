package org.springframework.samples.ntfh.enemy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/hordeEnemies")
public class HordeEnemyController {

    @Autowired
    private HordeEnemyService hordeEnemyService;

    @RequestMapping(method = RequestMethod.GET)
    public String listHordeEnemys(ModelMap model) {
        model.addAttribute("hordeEnemies", hordeEnemyService.findAll());
        return "hordeEnemies/list"; // TODO change returned view
    }

    @RequestMapping(value = "/{hordeEnemyId}", method = RequestMethod.GET)
    public String showHordeEnemy(@PathVariable("hordeEnemyId") int hordeEnemyId, ModelMap model) {
        model.addAttribute("hordeEnemy", hordeEnemyService.findHordeEnemyById(hordeEnemyId));
        return "hordeEnemys/show"; // TODO change returned view
    }
}