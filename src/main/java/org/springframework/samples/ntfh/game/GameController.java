package org.springframework.samples.ntfh.game;



import java.util.Optional;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @GetMapping()
    public String getAll(ModelMap  modelMap){
        String vista="games/gameList";
        Iterable<GameEntity> games=gameService.findAll(); 
        modelMap.addAttribute("games",games);
        return vista;
    }

    @GetMapping(path="/new")
    public String createGame(ModelMap modelMap){
        String view="games/editGame";
        modelMap.addAttribute("game", new GameEntity());
        return view;
    }


    @PostMapping(path="/save")
    public String saveGame(@Valid GameEntity game, BindingResult result, ModelMap modelMap){
        String view="games/gameList";
        if(result.hasErrors()){
            modelMap.addAttribute("game", game);
            return "games/editGame";
        }else{
            gameService.save(game);
            modelMap.addAttribute("message", "Game successfully saved");
            view= getAll(modelMap);
        }
        return view;

    }

    @GetMapping(path="/delete/{gameId}")
    public String deleteGame(@PathVariable("gameId") int gameId, ModelMap modelMap){
        String view="games/gameList";
        Optional<GameEntity> game=gameService.findGameById(gameId);
        if(game.isPresent()){
            gameService.delete(game.get());
            modelMap.addAttribute("message", "Game successfully deleted!");
        }else{
            modelMap.addAttribute("message", "Game not found!");

        }
        return view;
    }

}
