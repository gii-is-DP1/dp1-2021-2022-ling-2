package org.springframework.ntfh.user.unregistered;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.turn.concretestates.EnemyState;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.ntfh.entity.turn.concretestates.RefreshState;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUser;
import org.springframework.ntfh.entity.user.unregistered.UnregisteredUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author alegestor
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class, EnemyState.class, RefreshState.class })
public class UnregisteredUserTest {

    @Autowired
    private UnregisteredUserService unregisteredUserService;

    // TODO test this dynamically. There should be no initial unregisteredUser rows
    // in the DB
    // Number of unregisteredUsers in the DB
    private final Integer INITIAL_COUNT = 1;

    @Test
    void testFindAll() {
        Integer count = Lists.newArrayList(unregisteredUserService.findAll()).size();
        assertEquals(INITIAL_COUNT, count);
    }

    @Test
    void testFindById() {
        UnregisteredUser tester = this.unregisteredUserService.findUnregisteredUserById("user0023").get();
        assertEquals(1637882596427L, tester.getCreationTime());
    }

    @Test
    void testCreateUnregisteredUser() {
        Integer preCreationCount = Lists.newArrayList(unregisteredUserService.findAll()).size();
        unregisteredUserService.create();
        Integer posCreationCount = Lists.newArrayList(unregisteredUserService.findAll()).size();
        assertEquals(preCreationCount + 1, posCreationCount);
    }

    @Test
    void testDeleteUnregisteredUser() {
        UnregisteredUser toBeDeleted = unregisteredUserService.create();
        Integer preDeletionCount = Lists.newArrayList(unregisteredUserService.findAll()).size();
        unregisteredUserService.delete(toBeDeleted);
        Integer posCreationCount = Lists.newArrayList(unregisteredUserService.findAll()).size();
        assertEquals(preDeletionCount - 1, posCreationCount);
    }

}
