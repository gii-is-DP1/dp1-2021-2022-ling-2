package org.springframework.samples.ntfh.enemy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/warlords")
public class WarlordController {

    @Autowired
    private WarlordService warlordService;

    @RequestMapping(method = RequestMethod.GET)
    public String listWarlords(ModelMap model) {
        model.addAttribute("warlords", warlordService.findAll());
        return "warlords/list"; // TODO change returned view
    }

    @RequestMapping(value = "/{warlordId}", method = RequestMethod.GET)
    public String showWarlord(@PathVariable("warlordId") int warlordId, ModelMap model) {
        model.addAttribute("warlord", warlordService.findWarlordById(warlordId));
        return "warlords/show"; // TODO change returned view
    }
}
