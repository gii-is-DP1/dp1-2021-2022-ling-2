package org.springframework.samples.petclinic.scenarioNTFH;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ScenarioNTFHServiceTest {
    
    @Autowired
    private ScenarioNTFHService scenarioNTFHService;

    @Test
    public void testCountWithInitialData(){
        Integer count = scenarioNTFHService.scenarioNTFHCount();
        assertEquals(count, 0);
    }

}
