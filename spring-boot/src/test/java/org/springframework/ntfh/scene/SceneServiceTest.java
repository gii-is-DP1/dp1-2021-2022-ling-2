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
import org.springframework.ntfh.util.State;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = {@ComponentScan.Filter(Service.class), @ComponentScan.Filter(State.class)})
@Import({BCryptPasswordEncoder.class})
class SceneServiceTest {

    @Autowired
    private SceneService sceneService;

    protected Integer ALL_SCENES = 12;

    @Test
    void testCountWithInitialData() {
        Integer count = sceneService.count();

        assertThat(count).isEqualTo(ALL_SCENES);
    }

    @Test
    void testfindById() {
        Scene tester = this.sceneService.findSceneById(8).get();

        assertThat(tester.getSceneTypeEnum()).isEqualTo(SceneTypeEnum.PORTAL_DE_ULTHAR);
    }

}
