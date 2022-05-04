import org.comp2211.calculations.Runway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InputValidationTest {

  @Test
  public void testNegValues() {
    String name = "R21";
    int originalLda = 12;
    int displacedThreshold = 49;

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Runway(name, -22, originalLda, displacedThreshold));
  }

  @Test
  public void testZeroValues() {
    String name = "R21";
    int originalTora = 122;
    int displacedThreshold = 49;

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new Runway(name, originalTora, 0, displacedThreshold));
  }
}
