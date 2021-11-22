package org.springframework.samples.ntfh.marketcard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(value = "/marketCards")
public class MarketCardController {

    private final MarketCardService marketCardService;

    @Autowired
    public MarketCardController(MarketCardService marketCardService) {
        this.marketCardService = marketCardService;
    }

    @GetMapping
    public ResponseEntity<Iterable<MarketCard>> getAll() {
        // tested in MarketCardServiceTest
        Iterable<MarketCard> marketCards = this.marketCardService.findAll();
        return new ResponseEntity<>(marketCards, HttpStatus.OK);
    }

}
