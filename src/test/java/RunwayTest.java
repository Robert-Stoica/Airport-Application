import static org.junit.jupiter.api.Assertions.assertEquals;

import org.comp2211.calculations.Runway;
import org.junit.jupiter.api.Test;

class RunwayTest {

  @Test
  public void testRunwayProperties() {
    String name = "R21";
    int originalTora = 122;
    int originalLda = 12;
    int displacedThreshold = 49;

    Runway r = new Runway(name, originalTora, originalLda, displacedThreshold);

    assertEquals(name, r.getName());
    assertEquals(originalTora, r.getOriginalTora());
    assertEquals(originalLda, r.getOriginalLda());
    assertEquals(displacedThreshold, r.getDisplacedThreshold());
  }
}
