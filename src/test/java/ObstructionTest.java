import static org.junit.jupiter.api.Assertions.assertEquals;

import org.comp2211.calculations.Obstruction;
import org.junit.jupiter.api.Test;

class ObstructionTest {

  @Test
  public void testObstructionProperties() {
    int height = 25;
    int distanceFromCl = 10;
    int distanceFromThresh = 100;

    Obstruction o = new Obstruction(distanceFromCl, height, distanceFromThresh);

    assertEquals(height, o.getHeight());
    assertEquals(distanceFromCl, o.getDistanceFromCl());
    assertEquals(distanceFromThresh, o.getDistanceFromThresh());
  }
}
