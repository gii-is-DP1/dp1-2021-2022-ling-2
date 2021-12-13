package org.springframework.ntfh.entity.proficiency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/proficiencies")
public class ProficiencyController {

    @Autowired
    private ProficiencyService proficiencyService;

    @GetMapping()
    public ResponseEntity<Iterable<Proficiency>> getAll() {
        // TODO untested
        // We don't know if this method will be useful later
        Iterable<Proficiency> proficiencies = proficiencyService.findAll();
        return new ResponseEntity<>(proficiencies, HttpStatus.OK);
    }

    @GetMapping("count")
    public ResponseEntity<Integer> getCount() {
        Integer proficiencyCount = proficiencyService.count();
        return new ResponseEntity<>(proficiencyCount, HttpStatus.OK);
    }

}