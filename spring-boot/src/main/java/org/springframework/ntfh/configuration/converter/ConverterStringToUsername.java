package org.springframework.ntfh.configuration.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class ConverterStringToUsername implements Converter<String, User>{

    @Autowired
    UserService userService;
    
    @Override
    public User convert(String username) {
        return userService.findUser(username);
    }
}
