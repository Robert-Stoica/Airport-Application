import org.comp2211.calculations.Obstruction;
import org.comp2211.calculations.Runway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidationTest {


    @Test
    public void testRunwayNullName()
    {
        String name = "R21";
        int originalTora = 122;
        int originalLda = 12;
        int displacedThreshold = 49;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Runway(null, originalTora,originalLda,displacedThreshold);
        });
    }

    @Test
    public void testRunwayEmptyName()
    {
        String name = "R21";
        int originalTora = 122;
        int originalLda = 12;
        int displacedThreshold = 49;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Runway("", originalTora,originalLda,displacedThreshold);
        });
    }

    @Test
    public void testNegValues()
    {
        String name = "R21";
        int originalTora = 122;
        int originalLda = 12;
        int displacedThreshold = 49;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Runway(name, -22,originalLda,displacedThreshold);
        });

    }

    @Test
    public void testZeroValues()
    {
        String name = "R21";
        int originalTora = 122;
        int originalLda = 12;
        int displacedThreshold = 49;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Runway(name, originalTora,0,displacedThreshold);
        });

    }
}