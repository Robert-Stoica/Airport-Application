import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculationsS3ATest {

  final ScenarioTester scenario = createScenario();

  ScenarioTester createScenario() {
    var s = new ScenarioTester();
    s.setRunwayParams("09R", 3660, 3353, 307);
    s.setObstructionParams(60, 15, 150);
    s.calculate();
    return s;
  }

  // Scenario 2
  @Test
  void recalculateToraTowardsTest() {
    assertEquals(2903, scenario.redecRunwayAO.getTora());
  }

  @Test
  void recalculateAsdaTowardsTest() {
    assertEquals(2903, scenario.redecRunwayAO.getAsda());
  }

  @Test
  void recalculateTodaTowardsTest() {
    assertEquals(2903, scenario.redecRunwayAO.getToda());
  }

  @Test
  void recalculateLdaTowardsTest() {
    assertEquals(2393, scenario.redecRunwayAO.getLda());
  }
}
