package org.springframework.ntfh.user.unregistered;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUser;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUserService;
import org.springframework.stereotype.Service;

/**
 * @author alegestor
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UnregisteredUserTest {
    
    @Autowired
    private UnregisteredUserService unregisteredUserService;

    @Test
    void testFindAll() {
        Integer count = Lists.newArrayList(unregisteredUserService.findAll()).size();
        assertEquals(1, count);
    }

    @Test
    void testFindById() {
        UnregisteredUser tester = this.unregisteredUserService.findUnregisteredUserById("user0023").get();
        assertEquals(1637882596427l, tester.getCreationTime());
    }

}
