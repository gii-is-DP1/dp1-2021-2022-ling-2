package org.springframework.samples.ntfh.playablecard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/cards")
public class PlayableCardController {

    @Autowired
    private PlayableCardService playableCardService;

    @GetMapping
    public ResponseEntity<Iterable<PlayableCard>> getAll() {
        // untested
        Iterable<PlayableCard> playableCards = this.playableCardService.findAll();
        return new ResponseEntity<>(playableCards, HttpStatus.OK);
    }

}
