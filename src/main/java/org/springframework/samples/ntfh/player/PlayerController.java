package org.springframework.samples.ntfh.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

// TODO change to rest controller and return JSON
@Controller
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // Cannot use more than one @GetMapping()
    public String getAll(ModelMap modelMap) {
        String view = "players/listPlayer";
        Iterable<Player> players = playerService.findAll();
        modelMap.addAttribute("players", players);
        return view;
    }

}
