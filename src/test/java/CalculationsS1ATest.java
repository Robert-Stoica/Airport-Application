import org.junit.jupiter.api.Test;
import org.comp2211.Calculations.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculationsS1ATest {

    ScenarioTester scenario = createScenario();

    ScenarioTester createScenario(){
        var s = new ScenarioTester();
        s.setRunwayParams("09L", 3902, 3595, 306);
        s.setObstructionParams(0, 12, -50);
        s.calculate();
        return s;
    }

    // Scenario 1
    @Test
    void recalculateToraAwayOverTest() {
        assertEquals(3346, scenario.redecRunwayAO.getTora());
    }
    @Test
    void recalculateAsdaAwayOverTest() {
        assertEquals(3346, scenario.redecRunwayAO.getAsda());
    }

    @Test
    void recalculateTodaAwayOverTest() {
        assertEquals(3346, scenario.redecRunwayAO.getToda());
    }

    @Test
    void recalculateLdaAwayOverTest() {
        assertEquals(2985, scenario.redecRunwayAO.getLda());
    }


}