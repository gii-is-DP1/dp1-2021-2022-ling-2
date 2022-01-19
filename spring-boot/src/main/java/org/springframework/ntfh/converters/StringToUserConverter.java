package org.springframework.ntfh.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.ntfh.util.TokenUtils;
import org.springframework.stereotype.Component;

@Component
public class StringToUserConverter implements Converter<String, User> {

    @Autowired
    private UserService userService;

    @Override
    public User convert(String source) {
        String username =
                source.startsWith("Bearer ") ? TokenUtils.usernameFromToken(source) : source;
        return userService.findUser(username);
    }

}
