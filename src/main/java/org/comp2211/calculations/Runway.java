package org.comp2211.calculations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Runway {
  private final String name;
  private final int originalTora;
  private final int originalLda;
  private final int resa;
  private final int stripEnd;
  private int tora;
  private int toda;
  private int asda;
  private int lda;
  private int displacedThreshold;
  private int clearway;
  private int stopway;
  private int bProtection;
  private static final Logger logger = LogManager.getLogger(Runway.class);

  public Runway(String name, int tora, int lda, int dThreshold) {

    if (tora <= 0 || lda <= 0)
      throw new IllegalArgumentException();

    logger.info("Created a new Runway");
    this.name = name;
    this.originalTora = tora;
    this.originalLda = lda;
    this.tora = tora;
    this.lda = lda;
    this.displacedThreshold = dThreshold;
    this.clearway = 0;
    this.stopway = 0;
    this.resa = 240;
    this.stripEnd = 60;
    this.bProtection = 300;
  }

  public String getName() {
    return name;
  }

  public int getOriginalTora() {
    return originalTora;
  }

  public int getOriginalLda() {
    return originalLda;
  }

  public int getTora() {
    return tora;
  }

  public void setTora(Integer tora) {
    this.tora = tora;
  }

  public int getToda() {
    return toda;
  }

  public void setToda(Integer toda) {
    this.toda = toda;
  }

  public int getAsda() {
    return asda;
  }

  public void setAsda(Integer asda) {
    this.asda = asda;
  }

  public int getLda() {
    return lda;
  }

  public void setLda(Integer lda) {
    this.lda = lda;
  }

  public int getClearway() {
    return clearway;
  }

  public void setClearway(Integer clearway) {
    this.clearway = clearway;
  }

  public int getStopway() {
    return stopway;
  }

  public void setStopway(Integer stopway) {
    this.stopway = stopway;
  }

  public int getResa() {
    return resa;
  }

  public int getDisplacedThreshold() {
    return displacedThreshold;
  }

  public void setDisplacedThreshold(int displacedThreshold) {
    this.displacedThreshold = displacedThreshold;
  }

  public int getStripEnd() {
    return stripEnd;
  }

  public int getbProtection() {
    return bProtection;
  }

  public void setbProtection(int bProtection) {
    this.bProtection = bProtection;
  }
}
