package org.springframework.samples.ntfh.user.unregistered;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author andrsdt
 */
@Controller
public class UnregisteredUserController {

    @Autowired
    private UnregisteredUserService unregisteredUserService;
}