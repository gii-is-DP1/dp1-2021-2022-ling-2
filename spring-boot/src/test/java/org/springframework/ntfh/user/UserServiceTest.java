package org.springframework.ntfh.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ntfh.entity.user.User;
import org.springframework.ntfh.entity.user.UserRepository;
import org.springframework.ntfh.entity.user.UserService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testPH3E1() {
        List<User> PetitionUsers = new ArrayList<>();
        List<User> RepositoryUsers = new ArrayList<>();
        userService.findAll().forEach(x -> PetitionUsers.add(x));
        userRepository.findAll().forEach(y -> RepositoryUsers.add(y));
        assertEquals(RepositoryUsers.size(), PetitionUsers.size());
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(userService.findAll()).size();
        assertEquals(17, count);
    }

    @Test
    public void testfindById() {
        User tester = this.userService.findUser("stockie").orElse(null);
        assertEquals("stockie", tester.getPassword());
        assertEquals("stockie@mail.com", tester.getEmail());
    }

}
