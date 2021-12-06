package org.springframework.ntfh.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ntfh.entity.scene.Scene;
import org.springframework.ntfh.entity.scene.SceneService;
import org.springframework.ntfh.entity.scene.SceneTypeEnum;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class SceneServiceTest {

    @Autowired
    private SceneService sceneService;

    @Test
    public void testCountWithInitialData() {
        // TODO: Delete all and create mock initial data. Then test count.
        // By doing this we will make this test independent of the initial data.
        Integer count = sceneService.count();
        assertEquals(12, count);
    }

    @Test
    public void testfindAll() {
        Integer count = Lists.newArrayList(sceneService.findAll()).size();
        assertEquals(12, count);
    }

    @Test
    public void testfindById() {
        Scene tester = this.sceneService.findSceneById(8).orElse(null);
        assertEquals(SceneTypeEnum.PORTAL_DE_ULTHAR, tester.getSceneTypeEnum());
    }

}
