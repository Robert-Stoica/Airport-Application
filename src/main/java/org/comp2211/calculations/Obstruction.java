package org.comp2211.calculations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Obstruction {

  private final int distanceFromCl;
  private final int height;
  private final int distanceFromThresh;
    private static final Logger logger = LogManager.getLogger(Obstruction.class);

  public Obstruction(int distanceFromCl, int height, int distanceFromThresh) {
      logger.info("Created a new Obstruction");
    this.distanceFromCl = distanceFromCl;
    this.height = height;
    this.distanceFromThresh = distanceFromThresh;
  }

  public int getDistanceFromCl() {
    return distanceFromCl;
  }

  public int getDistanceFromThresh() {
    return distanceFromThresh;
  }

  public int getHeight() {
    return height;
  }
}
