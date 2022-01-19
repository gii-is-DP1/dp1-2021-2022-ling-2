package org.springframework.ntfh.entity.playablecard.marketcard.ingame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ntfh.entity.game.Game;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/market-cards")
public class MarketCardIngameController {
    


    @Autowired
    private MarketCardIngameService marketCardIngameService;


    @PostMapping("/buy/{marketCardIngameId}")
    public ResponseEntity<Game> buyMarketCard(@PathVariable("marketCardIngameId") Integer marketCardIngameId, 
            @RequestHeader("Authorization") String token) {
        Game game = marketCardIngameService.buyMarketCard(marketCardIngameId, token);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }






}
