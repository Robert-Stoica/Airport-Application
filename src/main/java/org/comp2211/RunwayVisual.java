package org.comp2211;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.comp2211.calculations.Calculations;
import org.comp2211.calculations.Runway;

public class RunwayVisual {

  public static boolean isAwayOver;
  private final String formatAO =
      """
          TORA = Original TORA - Blast Protection - Obstacle distance from threshold - Displaced Threshold
          \t = %d - %d - %d - %d
          \t = %d
          ASDA = (R)TORA + STOPWAY
          \t = %d
          TODA = (R)TORA + CLEARWAY
          \t = %d
          LDA  = (O)LDA - Obstacle distance from threshold - Strip end - Slope calculation
          \t = %d - %d - %d - %d
          \t = %d""";
  private final String formatTT =
      """
          TORA = Obstacle distance from threshold - Slope Calculation - Strip end
          \t = %d - %d - %d
          \t = %d
          ASDA = (R)TORA
          \t = %d
          TODA = (R)TORA
          \t = %d
          LDA  = Obstacle distance from threshold - RESA - Strip end
          \t = %d - %d - %d
          \t = %d""";
  @FXML private Button calculation;
  @FXML private Button goback;
  @FXML private Button contrastB;
  @FXML private Label tora;
  @FXML private Label toda;
  @FXML private Label asda;
  @FXML private Label lda;
  private boolean highContrast = false;

  void safeWriteFile(String filename, String data) {
    try {
      FileWriter myWriter = new FileWriter(filename);
      myWriter.write(data);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public void createFile() {
    Calculations calc = new Calculations();
    var copyRunway =
        new Runway(
            "copy",
            App.runway.getOriginalTora(),
            App.runway.getOriginalLda(),
            App.runway.getDisplacedThreshold());
    var oTora = copyRunway.getTora();
    var oLda = copyRunway.getLda();
    var dThresh = copyRunway.getDisplacedThreshold();

    int tora;
    int asda;
    int toda;
    int lda;

    String calculationsString;

    if (isAwayOver) {
      calc.recalculateToraAwayOver(copyRunway, App.obstruction);
      tora = copyRunway.getTora();
      calc.recalculateAsdaAwayOver(copyRunway);
      asda = copyRunway.getAsda();
      calc.recalculateTodaAwayOver(copyRunway);
      toda = copyRunway.getToda();
      calc.recalculateLdaAwayOver(copyRunway, App.obstruction);
      lda = copyRunway.getLda();
      calculationsString =
          String.format(
              formatAO,
              oTora,
              copyRunway.getbProtection(),
              App.obstruction.getDistanceFromThresh(),
              dThresh,
              tora,
              asda,
              toda,
              oLda,
              App.obstruction.getDistanceFromThresh(),
              copyRunway.getStripEnd(),
              0,
              lda);
    } else {
      calc.recalculateToraTowards(copyRunway, App.obstruction);
      tora = copyRunway.getTora();
      calc.recalculateAsdaTowards(copyRunway);
      asda = copyRunway.getAsda();
      calc.recalculateTodaTowards(copyRunway);
      toda = copyRunway.getToda();
      calc.recalculateLdaTowards(copyRunway, App.obstruction);
      lda = copyRunway.getLda();
      calculationsString =
          String.format(
              formatTT,
              App.obstruction.getDistanceFromThresh(),
              0,
              copyRunway.getStripEnd(),
              tora,
              asda,
              toda,
              App.obstruction.getDistanceFromThresh(),
              copyRunway.getResa(),
              copyRunway.getStripEnd(),
              lda);
    }

    try {
      File myObj = new File("calculations.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
        safeWriteFile("filename.txt", calculationsString);
      } else {
        System.out.println("File already exists.");
        safeWriteFile("calculations.txt", calculationsString);
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  @FXML
  public void setLabel() {
    lda.setText(String.valueOf(App.runway.getLda()));
    tora.setText(String.valueOf(App.runway.getTora()));
    asda.setText(String.valueOf(App.runway.getAsda()));
    toda.setText(String.valueOf(App.runway.getToda()));
  }

  public void newRunway() throws IOException {
    App.setRoot("Input");
  }
  public void changeContrast() {
	  if(highContrast ) {
		  highContrast = false;
		  calculation.getStyleClass().clear();
		  goback.getStyleClass().clear();
		  contrastB.getStyleClass().clear();
		  calculation.getStyleClass().add("button");
		  goback.getStyleClass().add("button");
		  contrastB.getStyleClass().add("button");
	  }else {
		  highContrast = true;
		  calculation.getStyleClass().add("button2");
		  goback.getStyleClass().add("button2");
		  contrastB.getStyleClass().add("button2");
	  }
	  
  }
}
