package org.springframework.samples.ntfh.marketcard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO change to RestController and re-implement the methods to return ResponseEntity<?> with JSON data
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/marketCards") // TODO follow rest API naming conventions
public class MarketCardController {

    private final MarketCardService marketCardService;

    @Autowired
    public MarketCardController(MarketCardService marketCardService) {
        this.marketCardService = marketCardService;
    }

    @GetMapping
    public ResponseEntity<Iterable<MarketCard>> getAll() {
        // untested
        Iterable<MarketCard> marketCards = this.marketCardService.findAll();
        return new ResponseEntity<>(marketCards, HttpStatus.OK);
    }

}
