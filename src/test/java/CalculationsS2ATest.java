import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationsS2ATest {

    ScenarioTester scenario = createScenario();

    ScenarioTester createScenario(){
        var s = new ScenarioTester();
        s.setRunwayParams("09R", 3660, 3660, 307);
        s.setObstructionParams(20, 25, 2853);
        s.calculate();
        return s;
    }

    // Scenario 2
    @Test
    void recalculateToraTowardsTest() {
        assertEquals(1850, scenario.redecRunwayTT.getTora());
    }

    @Test
    void recalculateAsdaTowardsTest() {
        assertEquals(1850, scenario.redecRunwayTT.getAsda());
    }

    @Test
    void recalculateTodaTowardsTest() {
        assertEquals(1850, scenario.redecRunwayTT.getToda());
    }

    @Test
    void recalculateLdaTowardsTest() {
        assertEquals(2553, scenario.redecRunwayTT.getLda());
    }


}