package org.comp2211.calculations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An obstruction on the runway.
 *
 * @author snow6071
 */
public class Obstruction {

  private static final Logger logger = LogManager.getLogger(Obstruction.class);
  /** Distance of the obstruction parallel to the runway, in metres. */
  private final int distanceFromCl;
  /** Height of the obstruction, in metres. */
  private final int height;
  /** Distance of the obstruction perpendicular to the runway, in metres. */
  private final int distanceFromThresh;

  /**
   * Creates a new obstruction.
   *
   * @param distanceFromCl Distance of the obstruction parallel to the runway, in metres.
   * @param height Height of the obstruction, in metres.
   * @param distanceFromThresh Distance of the obstruction perpendicular to the runway, in metres.
   */
  public Obstruction(int distanceFromCl, int height, int distanceFromThresh) {

    logger.info("Created a new Obstruction");
    this.distanceFromCl = distanceFromCl;
    this.height = height;
    this.distanceFromThresh = distanceFromThresh;
  }

  /**
   * Gets the distance from the center line.
   *
   * @return Distance of the obstruction parallel to the runway, in metres.
   */
  public int getDistanceFromCl() {
    return distanceFromCl;
  }

  /**
   * Gets the distance from the threshold.
   *
   * @return Distance of the obstruction perpendicular to the runway, in metres.
   */
  public int getDistanceFromThresh() {
    return distanceFromThresh;
  }

  /**
   * Get the height of the obstruction.
   *
   * @return Height of the obstruction, in metres.
   */
  public int getHeight() {
    return height;
  }

}
