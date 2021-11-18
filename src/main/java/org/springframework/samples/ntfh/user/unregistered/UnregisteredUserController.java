package org.springframework.samples.ntfh.user.unregistered;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrsdt
 */
@RestController()
@RequestMapping(value = "/unregistered-users")
public class UnregisteredUserController {

    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public UnregisteredUserController(UnregisteredUserService unregisteredUserService) {
        this.unregisteredUserService = unregisteredUserService;
    }

    /**
     * Will be called when a user accesses the application without being logged in
     */
    @GetMapping // Maybe make this a post? it creates an entry in the DB
    public ResponseEntity<UnregisteredUser> getCredentials() {
        UnregisteredUser unregisteredUser = unregisteredUserService.create();
        return new ResponseEntity<>(unregisteredUser, HttpStatus.OK);
    }
}