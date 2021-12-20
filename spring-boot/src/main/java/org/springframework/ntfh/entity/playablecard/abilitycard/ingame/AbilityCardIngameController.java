package org.springframework.ntfh.entity.playablecard.abilitycard.ingame;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param entity
     * @return
     */
    @PostMapping
    public ResponseEntity<Object> playCard(@RequestBody Map<String, String> input) {
        // TODO change input type
        return null;
    }

}
