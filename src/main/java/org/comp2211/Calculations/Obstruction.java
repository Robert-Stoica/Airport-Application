package org.comp2211.Calculations;

public class Obstruction {

  private final int distanceFromCl;
  private final int height;
  private final int distanceFromThresh;

  public Obstruction(int distanceFromCl, int height, int distanceFromThresh) {
    this.distanceFromCl = distanceFromCl;
    this.height = height;
    this.distanceFromThresh = distanceFromThresh;
  }
  public int getDistanceFromCl() {
    return distanceFromCl;
  }
  public int getDistanceFromTresh() {
        return distanceFromThresh;
    }
  public int getHeight() {
    return height;
  }

}
