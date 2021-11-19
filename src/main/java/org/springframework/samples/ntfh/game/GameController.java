package org.springframework.samples.ntfh.game;



import java.io.StringReader;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost;3000")
@RequestMapping("/games")
public class GameController {
    
    @Autowired
    private GameService gameService;

    @GetMapping()
    public ResponseEntity<Iterable<Game>> getAll(){
        Iterable<Game> games=gameService.findAll(); 
        return new ResponseEntity<>(games,HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<Map<String,String>> createGame(@Valid @RequestBody Game game){
        gameService.save(game);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


   // @PostMapping(path="/save")
   // public String saveGame(@Valid Game game, BindingResult result, ModelMap modelMap){
   //     String view="games/gameList";
   //     if(result.hasErrors()){
   //         modelMap.addAttribute("game", game);
   //         return "games/editGame";
   //     }else{
   //         gameService.save(game);
  //          modelMap.addAttribute("message", "Game successfully saved");
  //          view= getAll(modelMap);
   //     }
   //     return view;
    //}
    //HABRIA QUE ELIMINAR ESTOS DOS METODOS?
    // Y HACER UNO PARA EL JOIN
    @GetMapping(path="/delete/{gameId}")
    public ResponseEntity<Map<String,String>> deleteGame(@PathVariable("gameId") int gameId, ModelMap modelMap){
        String view="games/gameList";
        Optional<Game> game=gameService.findGameById(gameId);
        if(game.isPresent()){
            gameService.delete(game.get());
            modelMap.addAttribute("message", "Game successfully deleted!");
        }else{
            modelMap.addAttribute("message", "Game not found!");

        }
        return view;
    }

}
