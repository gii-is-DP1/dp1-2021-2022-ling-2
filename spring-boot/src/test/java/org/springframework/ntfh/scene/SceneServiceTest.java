package org.springframework.ntfh.scene;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.ntfh.entity.scene.Scene;
import org.springframework.ntfh.entity.scene.SceneService;
import org.springframework.ntfh.entity.scene.SceneTypeEnum;
import org.springframework.ntfh.entity.turn.concretestates.MarketState;
import org.springframework.ntfh.entity.turn.concretestates.PlayerState;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@Import({ BCryptPasswordEncoder.class, PlayerState.class, MarketState.class })
public class SceneServiceTest {

    @Autowired
    private SceneService sceneService;

    @Test
    public void testCountWithInitialData() {
        // TODO: Delete all and create mock initial data. Then test count.
        // By doing this we will make this test independent of the initial data.
        Integer count = sceneService.count();
        assertThat(count).isEqualTo(12);
    }

    @Test
    public void testfindById() {
        Scene tester = this.sceneService.findSceneById(8).orElse(null);
        assertThat(tester.getSceneTypeEnum()).isEqualTo(SceneTypeEnum.PORTAL_DE_ULTHAR);
    }

}
