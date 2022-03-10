import org.comp2211.calculations.Calculations;
import org.comp2211.calculations.Obstruction;
import org.comp2211.calculations.Runway;

public class ScenarioTester {
  public Runway runway;
  public Runway redecRunwayAO;
  public Runway redecRunwayTT;
  String r_name;
  int r_tora;
  int r_lda;
  int r_dthreshold;
  int o_distanceFromCL;
  int o_height;
  int o_distanceFromThresh;

  public ScenarioTester() {}

  public void setRunwayParams(String name, int tora, int lda, int dthreshold) {
    this.r_name = name;
    this.r_tora = tora;
    this.r_lda = lda;
    this.r_dthreshold = dthreshold;
  }

  public void setObstructionParams(int distanceFromCL, int height, int distanceFromThresh) {
    this.o_distanceFromCL = distanceFromCL;
    this.o_height = height;
    this.o_distanceFromThresh = distanceFromThresh;
  }

  public void calculate() {
    runway = createRunway();
    redecRunwayAO = createRedecRunwayAO();
    redecRunwayTT = createRedecRunwayTT();
  }

  Runway createRunway() {
    return new Runway(r_name, r_tora, r_lda, r_dthreshold);
  }

  Runway createRedecRunwayAO() {
    var r = createRunway();
    var o = new Obstruction(o_distanceFromCL, o_height, o_distanceFromThresh);
    var calculations = new Calculations();
    calculations.recalculateToraAwayOver(r, o);
    calculations.recalculateAsdaAwayOver(r);
    calculations.recalculateTodaAwayOver(r);
    calculations.recalculateLdaAwayOver(r, o);
    return r;
  }

  Runway createRedecRunwayTT() {
    var r = createRunway();
    var o = new Obstruction(o_distanceFromCL, o_height, o_distanceFromThresh);
    var calculations = new Calculations();
    calculations.recalculateToraTowards(r, o);
    calculations.recalculateAsdaTowards(r);
    calculations.recalculateTodaTowards(r);
    calculations.recalculateLdaTowards(r, o);
    return r;
  }
}
