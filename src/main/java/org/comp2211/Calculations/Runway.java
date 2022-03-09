package org.comp2211.Calculations;

public class Runway {
  private int tora;
  private int toda;
  private int asda;
  private int lda;
  private int dThreshold;
  private int clearway;
  private int stopway;
  private final int resa;
  private final int stripEnd;
  private final int bProtection;
  private final int als;
  private final int tocs;
  private final int runStrip;
  private final String runDesignator;
  private final int distanceFromCl;
  private final int distanceFromRun;

  public Runway(
      int tora,
      int toda,
      int asda,
      int lda,
      int dThreshold,
      int clearway,
      int stopway,
      int resa,
      int stripEnd,
      int bProtection,
      int als,
      int tocs,
      int runStrip,
      String runDesignator,
      int distanceFromCl,
      int distanceFromRun) {
    this.tora = tora;
    this.toda = toda;
    this.asda = asda;
    this.lda = lda;
    this.dThreshold = dThreshold;
    this.clearway = clearway;
    this.stopway = stopway;
    this.resa = resa;
    this.stripEnd = stripEnd;
    this.bProtection = bProtection;
    this.als = als;
    this.tocs = tocs;
    this.runStrip = runStrip;
    this.runDesignator = runDesignator;
    this.distanceFromCl = distanceFromCl;
    this.distanceFromRun = distanceFromRun;
  }

  public int getTora() {
    return tora;
  }

  public int getToda() {
    return toda;
  }

  public int getAsda() {
    return asda;
  }

  public int getLda() {
    return lda;
  }

  public int getdThreshold() {
    return dThreshold;
  }

  public int getClearway() {
    return clearway;
  }

  public int getStopway() {
    return stopway;
  }

  public int getResa() {
    return resa;
  }

  public int getStripEnd() {
    return stripEnd;
  }

  public int getbProtection() {
    return bProtection;
  }

  public int getAls() {
    return als;
  }

  public int getTocs() {
    return tocs;
  }

  public int getRunStrip() {
    return runStrip;
  }

  public String getRunDesignator() {
    return runDesignator;
  }

  public int getDistanceFromCl() {
    return distanceFromCl;
  }

  public int setTora(Integer tora) {
    return this.tora = tora;
  }

  public int setToda(Integer toda) {
    return this.toda = toda;
  }

  public int setAsda(Integer asda) {
    return this.asda = asda;
  }

  public int setLda(Integer lda) {
    return this.lda = lda;
  }

  public int setTreshold(Integer tresh) {
        return this.dThreshold = tresh;
    }

  public int setClearway(Integer clearway) {
        return this.clearway = clearway;
    }

  public int setStopway(Integer stopway) {
        return this.stopway = stopway;
    }
}
