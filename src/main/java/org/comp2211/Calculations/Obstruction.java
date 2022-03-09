package org.comp2211.Calculations;

public class Obstruction {

  private final int distanceFromCl;
  private final int height;

  public Obstruction(int distanceFromCl, int height) {
    this.distanceFromCl = distanceFromCl;
    this.height = height;
  }
  public int getDistanceFromCl() {
    return distanceFromCl;
  }
  public int getHeight() {
    return height;
  }
}
