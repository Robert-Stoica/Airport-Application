import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculationsS2BTest {

  ScenarioTester scenario = createScenario();

  ScenarioTester createScenario() {
    var s = new ScenarioTester();
    s.setRunwayParams("27L", 3660, 3660, 0);
    s.setObstructionParams(20, 25, 500);
    s.calculate();
    return s;
  }

  // Scenario 2
  @Test
  void recalculateToraTowardsTest() {
    assertEquals(2860, scenario.redecRunwayAO.getTora());
  }

  @Test
  void recalculateAsdaTowardsTest() {
    assertEquals(2860, scenario.redecRunwayAO.getAsda());
  }

  @Test
  void recalculateTodaTowardsTest() {
    assertEquals(2860, scenario.redecRunwayAO.getToda());
  }

  @Test
  void recalculateLdaTowardsTest() {
    assertEquals(1850, scenario.redecRunwayAO.getLda());
  }
}
