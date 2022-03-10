import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculationsS3BTest {

  ScenarioTester scenario = createScenario();

  ScenarioTester createScenario() {
    var s = new ScenarioTester();
    s.setRunwayParams("27L", 3660, 3660, 0);
    s.setObstructionParams(60, 15, 3203);
    s.calculate();
    return s;
  }

  // Scenario 2
  @Test
  void recalculateToraTowardsTest() {
    assertEquals(2393, scenario.redecRunwayTT.getTora());
  }

  @Test
  void recalculateAsdaTowardsTest() {
    assertEquals(2393, scenario.redecRunwayTT.getAsda());
  }

  @Test
  void recalculateTodaTowardsTest() {
    assertEquals(2393, scenario.redecRunwayTT.getToda());
  }

  @Test
  void recalculateLdaTowardsTest() {
    assertEquals(2903, scenario.redecRunwayTT.getLda());
  }
}
