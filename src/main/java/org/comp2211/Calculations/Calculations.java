package org.comp2211.Calculations;

public class Calculations {

  public Runway recalculateToraTowards(Runway runway, Obstruction obstruction) {
    int tora =
        obstruction.getDistanceFromCl()
            + runway.getDisplacedThreshold()
            - (obstruction.getHeight() * obstruction.getDistanceFromCl())
            - runway.getStripEnd();
    runway.setTora(tora);
    return runway;
  }

  public Runway recalculateToraAwayOver(Runway runway, Obstruction obstruction) {
    int tora =
        runway.getTora()
            - obstruction.getDistanceFromCl()
            - runway.getDisplacedThreshold()
            - runway.getResa()
            - runway.getStripEnd();
    runway.setTora(tora);
    return runway;
  }

  public Runway RunwayrecalculateTodaTowards(Runway runway) {
    int toda = runway.getTora();
    runway.setToda(toda);
    return runway;
  }

  public Runway recalculateTodaAwayOver(Runway runway) {
    int toda = runway.getTora() + runway.getStopway();
    runway.setToda(toda);
    return runway;
  }

  public Runway recalculateAsdaTowards(Runway runway) {
    int asda = runway.getTora() + runway.getClearway();
    runway.setAsda(asda);
    return runway;
  }

  public Runway recalculateAsdaAwayOver(Runway runway) {
    int asda = runway.getTora();
    runway.setAsda(asda);
    return runway;
  }

  public Runway recalculateLdaTowards(Runway runway, Obstruction obstruction) {
    int lda = obstruction.getDistanceFromCl() - runway.getResa() - runway.getStripEnd();
    runway.setLda(lda);
    return runway;
  }

  public Runway recalculateLdaAwayOver(Runway runway, Obstruction obstruction) {
    int lda =
        runway.getLda()
            - obstruction.getDistanceFromCl()
            - runway.getStripEnd()
            - (obstruction.getHeight() * obstruction.getDistanceFromCl());
    runway.setLda(lda);
    return runway;
  }
}
