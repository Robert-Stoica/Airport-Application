package org.comp2211.Calculations;

public class Runway {
  private final String name;
  private final int originalTora;
  private final int originalLda;
  private int tora;
  private int toda;
  private int asda;
  private int lda;
  private int displacedThreshold;
  private int clearway;
  private int stopway;
  private final int resa;
  private final int stripEnd;
  private final int bProtection;

  public Runway(
      String name,
      int tora,
      int lda,
      int dThreshold) {
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
  public int getOrigianlTora() {
    return originalTora;
  }
  public int getOriginalLda() {
    return originalLda;
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

  public int getClearway() {
    return clearway;
  }

  public int getStopway() {
    return stopway;
  }

  public int getResa() {
    return resa;
  }
  public int getDisplacedThreshold() {
	return displacedThreshold;
  }

  public int getStripEnd() {
    return stripEnd;
  }

  public int getbProtection() {
    return bProtection;
  }

  public void setTora(Integer tora) {
    this.tora = tora;
  }

  public void setToda(Integer toda) {
    this.toda = toda;
  }

  public void setAsda(Integer asda) {
    this.asda = asda;
  }

  public void setLda(Integer lda) {
    this.lda = lda;
  }

  public void setClearway(Integer clearway) {
    this.clearway = clearway;
  }

  public void setStopway(Integer stopway) {
    this.stopway = stopway;
  }

  public void setDisplacedThreshold(int displacedThreshold) {
	this.displacedThreshold = displacedThreshold;
}
}
