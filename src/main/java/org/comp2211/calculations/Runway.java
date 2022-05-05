package org.comp2211.calculations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A representation of a physical runway within the app. <b>All values given are in metres.</b>
 *
 * @author snow6071
 */
public class Runway {
  private static final Logger logger = LogManager.getLogger(Runway.class);
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
  /** "blast protection". */
  private int bProtection;

  /**
   * Make a new runway.
   *
   * @param name The name to give the runway.
   * @param tora The TORA of the runway
   * @param lda The LDA of the runway.
   * @param dThreshold the displaced threshold of the runway.
   */
  public Runway(String name, int tora, int lda, int dThreshold) {

    if (tora <= 0 || lda <= 0) {
      throw new IllegalArgumentException();
    }

    logger.info("Created a new Runway: "+name);
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

  /**
   * Gets the name of the runway.
   *
   * @return The name of the runway.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the original TORA, before any recalculations due to obstructions are made.
   *
   * @return The original TORA.
   */
  public int getOriginalTora() {
    return originalTora;
  }

  /**
   * Gets the original LDA, before any recalculations due to obstructions are made.
   *
   * @return The original LDA.
   */
  public int getOriginalLda() {
    return originalLda;
  }

  /**
   * Gets the TORA, after any recalculations due to obstructions are made.
   *
   * @return The TORA.
   */
  public int getTora() {
    return tora;
  }

  /**
   * Sets the TORA.
   *
   * @param tora The TORA to set.
   */
  public void setTora(Integer tora) {
    this.tora = tora;
  }

  /**
   * Gets the TODA.
   *
   * @return The TODA.
   */
  public int getToda() {
    return toda;
  }

  /**
   * Sets the TODA.
   *
   * @param toda The TODA to set.
   */
  public void setToda(Integer toda) {
    this.toda = toda;
  }

  /**
   * Gets the ASDA.
   *
   * @return The ASDA.
   */
  public int getAsda() {
    return asda;
  }

  /**
   * Sets the ASDA.
   *
   * @param asda The ASDA to set.
   */
  public void setAsda(Integer asda) {
    this.asda = asda;
  }

  /**
   * Gets the LDA, after any recalculations due to obstructions are made.
   *
   * @return The LDA.
   */
  public int getLda() {
    return lda;
  }

  /**
   * Sets the LDA.
   *
   * @param lda The LDA to set.
   */
  public void setLda(Integer lda) {
    this.lda = lda;
  }

  /**
   * Gets the clearway length.
   *
   * @return The clearway length.
   */
  public int getClearway() {
    return clearway;
  }

  /**
   * Sets the clearway length.
   *
   * @param clearway The clearway length to set.
   */
  public void setClearway(Integer clearway) {
    this.clearway = clearway;
  }

  /**
   * Gets the stopway length.
   *
   * @return The stopway length.
   */
  public int getStopway() {
    return stopway;
  }

  /**
   * Sets the stopway length.
   *
   * @param stopway The stopway length to set.
   */
  public void setStopway(Integer stopway) {
    this.stopway = stopway;
  }

  /**
   * Gets the RESA.
   *
   * @return The RESA.
   */
  public int getResa() {
    return resa;
  }

  /**
   * Gets the displaced threshold.
   *
   * @return The stopway length.
   */
  public int getDisplacedThreshold() {
    return displacedThreshold;
  }

  /**
   * Sets the displaced threshold.
   *
   * @param displacedThreshold The displaced threshold to set.
   */
  public void setDisplacedThreshold(int displacedThreshold) {
    this.displacedThreshold = displacedThreshold;
  }

  /**
   * Gets the strip end length.
   *
   * @return The strip end length.
   */
  public int getStripEnd() {
    return stripEnd;
  }

  /**
   * Gets the blast protection.
   *
   * @return The blast protection.
   */
  public int getbProtection() {
    return bProtection;
  }

  /**
   * Sets the blast protection.
   *
   * @param bProtection The blast protection to set.
   */
  public void setbProtection(int bProtection) {
    this.bProtection = bProtection;
  }
}
