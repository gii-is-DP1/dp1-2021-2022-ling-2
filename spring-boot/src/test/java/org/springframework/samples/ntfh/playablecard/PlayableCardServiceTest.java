package org.springframework.samples.ntfh.playablecard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlayableCardServiceTest {

    @Autowired
    private PlayableCardService playableCardService;

    @Test
    public void testCountWithInitialData() {
        Integer count = playableCardService.cardCount();
        assertEquals(0, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(playableCardService.findAll()).size();
        assertEquals(0, count);
    }

    @Test
    public void testfindById() {
        // TODO
    }

}
