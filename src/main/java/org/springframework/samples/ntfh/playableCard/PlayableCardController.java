package org.springframework.samples.ntfh.playableCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cards")
public class PlayableCardController {

    @Autowired
    private PlayableCardService playableCardService;

    @GetMapping()
    public String getAll(ModelMap modelMap) { // modelmap object contains the data that will be passed to the view
        String view = "cards/listCards";
        Iterable<PlayableCard> cards = playableCardService.findAll();
        modelMap.addAttribute("cards", cards);
        return view;
    }

}
