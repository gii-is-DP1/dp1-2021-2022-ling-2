package org.springframework.samples.ntfh.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cards")
public class CardController {
    
    @Autowired
    private CardService cardService;

    @GetMapping()
    public String getAll(ModelMap modelMap) { // modelmap object contains the data that will be passed to the view
        String view = "cards/listCards";
        Iterable<Card> cards = cardService.findAll();
        modelMap.addAttribute("cards", cards);
        return view;
    }

}
