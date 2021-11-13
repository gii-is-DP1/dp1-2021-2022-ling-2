package org.springframework.samples.ntfh.game;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @GetMapping()
    public String gameList(ModelMap  modelMap){
        String vista="games/gameList";
        Iterable<GameEntity> games=gameService.findAll(); 
        modelMap.addAttribute("games",games);
        return vista;
    }


}
