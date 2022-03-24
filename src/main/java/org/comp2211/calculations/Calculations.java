package org.comp2211.calculations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Various calculations related to obstructions on the runway.
 *
 * @author snow6071
 * @see Runway
 * @see Obstruction
 */
public class Calculations {

  private static final Logger logger = LogManager.getLogger(Calculations.class);

  /**
   * Recalculate the TORA on a runway, given that there is an obstruction at the opposite end of the
   * runway to a plane.
   *
   * @param runway The runway to recalculate in place.
   * @param obstruction The obstruction to recalculate around.
   * @return The recalculated runway.
   */
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

  /**
   * Recalculate the TORA on a runway, given that there is an obstruction at the same end of the
   * runway as a plane.
   *
   * @param runway The runway to recalculate in place.
   * @param obstruction The obstruction to recalculate around.
   * @return The recalculated runway.
   */
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

  /**
   * Recalculate the TODA on a runway, assuming the TORA has changed and an obstruction is at the
   * opposite end of the runway as a plane.
   *
   * @param runway The runway to recalculate in place.
   * @return The recalculated runway.
   */
  public Runway recalculateTodaTowards(Runway runway) {
      logger.info("Calculated Toda");
    int toda = runway.getTora();
    runway.setToda(toda);
    return runway;
  }

  /**
   * Recalculate the TODA on a runway, assuming the TORA has changed and an obstruction is at the
   * same end of the runway as a plane.
   *
   * @param runway The runway to recalculate in place.
   * @return The recalculated runway.
   */
  public Runway recalculateTodaAwayOver(Runway runway) {
      logger.info("Calculated Toda");
    int toda = runway.getTora() + runway.getStopway();
    runway.setToda(toda);
    return runway;
  }

  /**
   * Recalculate the ASDA on a runway, assuming the TORA has changed and an obstruction is at the
   * opposite end of the runway as a plane.
   *
   * @param runway The runway to recalculate in place.
   * @return The recalculated runway.
   */
  public Runway recalculateAsdaTowards(Runway runway) {
      logger.info("Calculated Asda");
    int asda = runway.getTora() + runway.getClearway();
    runway.setAsda(asda);
    return runway;
  }

  /**
   * Recalculate the ASDA on a runway, assuming the TORA has changed and an obstruction is at the
   * same end of the runway as a plane.
   *
   * @param runway The runway to recalculate in place.
   * @return The recalculated runway.
   */
  public Runway recalculateAsdaAwayOver(Runway runway) {
      logger.info("Calculated Asda");
    int asda = runway.getTora();
    runway.setAsda(asda);
    return runway;
  }

  /**
   * Recalculate the LDA on a runway, given that there is an obstruction at the opposite end of the
   * runway to a plane.
   *
   * @param runway The runway to recalculate in place.
   * @param obstruction The obstruction to recalculate around.
   * @return The recalculated runway.
   */
  public Runway recalculateLdaTowards(Runway runway, Obstruction obstruction) {
      logger.info("Calculated Lda");
    int lda = obstruction.getDistanceFromThresh() - runway.getResa() - runway.getStripEnd();
    runway.setLda(lda);
    return runway;
  }

  /**
   * Recalculate the LDA on a runway, given that there is an obstruction at the same end of the
   * runway as a plane.
   *
   * @param runway The runway to recalculate in place.
   * @param obstruction The obstruction to recalculate around.
   * @return The recalculated runway.
   */
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
