import static org.junit.jupiter.api.Assertions.assertEquals;

import org.comp2211.calculations.Calculations;
import org.comp2211.calculations.Obstruction;
import org.comp2211.calculations.Runway;
import org.junit.jupiter.api.Test;

class CalculationsTest {
  private final Calculations calculator = new Calculations();

  @Test
  public void recalculateToraTowardsTest() {
    Runway runway = runway();
    Obstruction obstruction = obstruction();
    Runway result = calculator.recalculateToraTowards(runway, obstruction);

    assertEquals(result.getTora(), 1850);
  }

  @Test
  public void recalculateToraAwayOverTest() {
    Runway runway = runway();
    Obstruction obstruction = obstruction();
    Runway result = calculator.recalculateToraAwayOver(runway, obstruction);

    assertEquals(result.getTora(), 200);
  }

  @Test
  public void recalculateTodaTowardsTest() {
    Runway runway = runway();
    Runway result = calculator.recalculateTodaTowards(runway);

    assertEquals(result.getToda(), runway.getTora());
  }

  @Test
  public void recalculateTodaAwayOverTest() {
    Runway runway = runway();
    Runway result = calculator.recalculateTodaAwayOver(runway);

    assertEquals(result.getToda(), runway.getTora());
  }

  @Test
  public void recalculateAsdaTowardsTest() {
    Runway runway = runway();
    Runway result = calculator.recalculateAsdaTowards(runway);

    assertEquals(result.getAsda(), runway.getTora());
  }

  @Test
  public void recalculateAsdaAwayOverTest() {
    Runway runway = runway();
    Runway result = calculator.recalculateAsdaAwayOver(runway);

    assertEquals(result.getAsda(), runway.getTora());
  }

  @Test
  public void recalculateLdaTowardsTest() {
    Runway runway = runway();
    Obstruction obstruction = obstruction();
    Runway result = calculator.recalculateLdaTowards(runway, obstruction);

    assertEquals(result.getLda(), 2553);
  }

  @Test
  public void recalculateLdaAwayOverTest() {
    Runway runway = runway();
    Obstruction obstruction = obstruction();
    Runway result = calculator.recalculateLdaAwayOver(runway, obstruction);

    assertEquals(result.getLda(), -503);
  }

  private Runway runway() {
    return new Runway("09R", 3660, 3660, 307);
  }

  private Obstruction obstruction() {
    return new Obstruction(20, 25, 2853);
  }
}
