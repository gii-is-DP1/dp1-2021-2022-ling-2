package org.springframework.ntfh.entity.playablecard.abilitycard.ingame;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/ability-cards")
public class AbilityCardIngameController {
    

    @Autowired
    private AbilityCardIngameService abilityCardIngameService;


    /**
     * This endpoint will receive the petitions of a player to play a card
     * 
     * @author andrsdt
     * @param entity with the information of the card to play
     * @return the game with the updated state
     */
    @PostMapping("/{abilityCardIngameId}")
    public ResponseEntity<Game> playCard(@PathVariable("abilityCardIngameId") Integer abilityCardIngameId,
            @RequestBody Map<String, Integer> body,
            @RequestHeader("Authorization") String token) {
        Integer enemyId = body.get("enemyId");
        Game game = abilityCardIngameService.playCard(abilityCardIngameId, enemyId, token);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }


}
