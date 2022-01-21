package org.springframework.ntfh.entity.user.unregistered;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andrsdt
 */
@RestController
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
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UnregisteredUser getCredentials() {
        UnregisteredUser unregisteredUser = unregisteredUserService.create();
        return unregisteredUser;
    }

}
