package org.springframework.samples.ntfh.user.unregistered;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UnregisteredController {
    
    @Autowired
    private UnregisteredService unregisteredService;

    // TODO UnregisteredController giving issues with "ambiguous mapping" with user

}