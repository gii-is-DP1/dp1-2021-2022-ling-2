package org.springframework.samples.ntfh.marketCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/marketCards")
public class MarketCardController {
    
    @Autowired
    private MarketCardService marketCardService;

    @GetMapping()
    public String getAll(ModelMap modelMap) { // modelmap object contains the data that will be passed to the view
        String view = "marketCards/listMarketCards";
        Iterable<MarketCard> marketCards = marketCardService.findAll();
        modelMap.addAttribute("marketCards", marketCards);
        return view;
    }

}
