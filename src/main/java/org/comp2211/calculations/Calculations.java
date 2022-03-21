package org.comp2211.calculations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comp2211.ObstacleInput;

public class Calculations {

    private static final Logger logger = LogManager.getLogger(Calculations.class);

  public Runway recalculateToraTowards(Runway runway, Obstruction obstruction) {
      logger.info("Calculated Tora");
    int tora =
        obstruction.getDistanceFromThresh()
            + runway.getDisplacedThreshold()
            - (obstruction.getHeight() * 50)
            - runway.getStripEnd();
    runway.setTora(tora);
    return runway;
  }

  public Runway recalculateToraAwayOver(Runway runway, Obstruction obstruction) {
      logger.info("Calculated Tora");
      int tora =
        runway.getTora()
            - obstruction.getDistanceFromThresh()
            - runway.getDisplacedThreshold()
            - runway.getResa()
            - runway.getStripEnd();
    runway.setTora(tora);
    return runway;
  }

  public Runway recalculateTodaTowards(Runway runway) {
      logger.info("Calculated Toda");
    int toda = runway.getTora();
    runway.setToda(toda);
    return runway;
  }

  public Runway recalculateTodaAwayOver(Runway runway) {
      logger.info("Calculated Toda");
    int toda = runway.getTora() + runway.getStopway();
    runway.setToda(toda);
    return runway;
  }

  public Runway recalculateAsdaTowards(Runway runway) {
      logger.info("Calculated Asda");
    int asda = runway.getTora() + runway.getClearway();
    runway.setAsda(asda);
    return runway;
  }

  public Runway recalculateAsdaAwayOver(Runway runway) {
      logger.info("Calculated Asda");
    int asda = runway.getTora();
    runway.setAsda(asda);
    return runway;
  }

  public Runway recalculateLdaTowards(Runway runway, Obstruction obstruction) {
      logger.info("Calculated Lda");
    int lda = obstruction.getDistanceFromThresh() - runway.getResa() - runway.getStripEnd();
    runway.setLda(lda);
    return runway;
  }

  public Runway recalculateLdaAwayOver(Runway runway, Obstruction obstruction) {
      logger.info("Calculated Lda");
    int lda =
        runway.getLda()
            - obstruction.getDistanceFromThresh()
            - runway.getStripEnd()
            - (obstruction.getHeight() * 50);
    runway.setLda(lda);
    return runway;
  }
}
