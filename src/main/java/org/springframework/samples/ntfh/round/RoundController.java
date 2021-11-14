package org.springframework.samples.ntfh.round;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rounds")
public class RoundController {
    
    @Autowired
    private RoundService roundService;

    @GetMapping()
    public String getAll(ModelMap modelMap) { // modelmap object contains the data that will be passed to the view
        String view = "rounds/listRounds";
        Iterable<Round> rounds = roundService.findAll();
        modelMap.addAttribute("rounds", rounds);
        return view;
    }

    @GetMapping(path = "/new")
    public String createRound(ModelMap modelMap) {
        String view = "rounds/editRound";
        modelMap.addAttribute("round", new Round()); // we add a new created round to the context so we can edit it
        return view;
    }

    @PostMapping(path = "/save")
    public String saveRound(@Valid Round round, BindingResult result, ModelMap modelMap) {
        String view = "rounds/listRounds";
        if (result.hasErrors()) {
            modelMap.addAttribute("round", round); // round with errors as new context to represent it again
            return "rounds/editRound"; // and we return to the form which should show the errors
        } else {
            roundService.save(round);
            modelMap.addAttribute("message", "Round successfully saved");
            view = getAll(modelMap); // after saving the round, we will udpate the view with the new data
        }
        return view;
    }

    @GetMapping(path = "/delete/{roundId}")
    public String deleteRound(@PathVariable("roundId") int roundId, ModelMap modelMap) {
        String view = "rounds/listRounds";
        Optional<Round> round = roundService.findRoundById(roundId);
        if (round.isPresent()) {
            roundService.delete(roundId);
            modelMap.addAttribute("message", "Round successfully deleted");
            view = getAll(modelMap); // after saving the round, we will udpate the view with the new data
        } else {
            modelMap.addAttribute("message", "Round not found");
        }
        return view;
    }
}
