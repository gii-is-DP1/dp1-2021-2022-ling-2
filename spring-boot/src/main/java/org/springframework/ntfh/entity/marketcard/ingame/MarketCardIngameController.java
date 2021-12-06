package org.springframework.ntfh.entity.marketcard.ingame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/market-cards")
public class MarketCardIngameController {

    @Autowired
    private MarketCardIngameService marketCardIngameService;

    @GetMapping("/{gameId}")
    public ResponseEntity<Iterable<MarketCardIngame>> getMarketCardsFromGame(@PathVariable("gameId") Integer gameId) {
        Iterable<MarketCardIngame> hordeEnemiesIngame = marketCardIngameService.findMarketCardsByGameId(gameId);
        return new ResponseEntity<>(hordeEnemiesIngame, HttpStatus.OK);
    }
}
