import org.junit.jupiter.api.Test;
import org.comp2211.Calculations.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculationsS1BTest {

    ScenarioTester scenario = createScenario();

    ScenarioTester createScenario(){
        var s = new ScenarioTester();
        s.setRunwayParams("27R", 3884, 3884, 0);
        s.setObstructionParams(0, 12, 3646);
        s.calculate();
        return s;
    }

    // Scenario 2
    @Test
    void recalculateToraTowardsTest() {
        assertEquals(2986, scenario.redecRunwayTT.getTora());
    }

    @Test
    void recalculateAsdaTowardsTest() {
        assertEquals(2986, scenario.redecRunwayTT.getAsda());
    }

    @Test
    void recalculateTodaTowardsTest() {
        assertEquals(2986, scenario.redecRunwayTT.getToda());
    }

    @Test
    void recalculateLdaTowardsTest() {
        assertEquals(3346, scenario.redecRunwayTT.getLda());
    }


}