package org.comp2211.Calculations;

public class Obstruction {

  private final String name;
  private final int distanceFromCl;
  private final int distanceAlongCl;
  private final int height;

  public Obstruction(String name, int distanceFromCl, int distanceAlongCl, int height) {
    this.name = name;
    this.distanceFromCl = distanceFromCl;
    this.distanceAlongCl = distanceAlongCl;
    this.height = height;
  }

  public String getName() {
    return name;
  }

  public int getDistanceFromCl() {
    return distanceFromCl;
  }

  public int getDistanceAlongCl() {
    return distanceAlongCl;
  }

  public int getHeight() {
    return height;
  }
}