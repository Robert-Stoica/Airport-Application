package org.comp2211;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comp2211.calculations.Calculations;
import org.comp2211.calculations.Runway;

/**
 * Displays a visual representation of the recalculated runway to the screen, using magic.
 *
 * @author JoshPattman
 */
public class RunwayVisual {

  private static final Logger logger = LogManager.getLogger(Calculations.class);
  public static boolean isAwayOver;
  final Color grassGreen = Color.color(51 / 255.0, 204 / 255.0, 51 / 255.0);
  final Color skyBlue = Color.color(85 / 255.0, 216 / 255.0, 255 / 255.0);
  final Color asphaltGrey = Color.color(150 / 255.0, 150 / 255.0, 150 / 255.0);
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
  public Button toggleToraButton;
  public Button toggleTodaButton;
  public Button toggleAsdaButton;
  public Button toggleResaButton;
  public Button toggleSeButton;
  public Button toggleH50Button;
  public Button toggleEbaButton;
  public Button toggleHButton;
  public Button toggleLdaButton;
  boolean isTakeoff;
  @FXML
  private Button calculation;
  @FXML
  private Button goback;
  @FXML
  private Button contrastB;
  @FXML
  private Label tora;
  @FXML
  private Label toda;
  @FXML
  private Label asda;
  @FXML
  private Label lda;
  @FXML
  private Canvas sideViewCanvas;
  @FXML
  private Canvas topViewCanvas;
  @FXML
  private MenuButton menu;
  @FXML
  private MenuItem landing;
  @FXML
  private MenuItem takeoff;
  @FXML
  private HBox manual;
  @FXML
  private Label slopeCalculation;
  @FXML
  private Label threshold;
  private double origHeight = -1;
  private boolean highContrast = false;

  private boolean showTora = true;
  private boolean showToda = true;
  private boolean showAsda = true;
  private boolean showResa = true;
  private boolean showSe = true;
  private boolean showH50 = true;
  private boolean showEba = true;
  private boolean showH = true;
  private boolean showLDA = true;

  /**
   * Creates a window with a message.
   *
   * @param infoMessage Message to display
   * @param titleBar Title of the window
   */
  public static void infoBox(String infoMessage, String titleBar) {
    Alert error2 = new Alert(Alert.AlertType.INFORMATION);
    error2.setTitle("Noticeboard");
    error2.setHeaderText(titleBar);
    error2.setContentText(infoMessage);
    error2.showAndWait();
  }

  /**
   * Wrap file handling in a safe function to avoid exceptions.
   *
   * @param filename The file to write to
   * @param data     The data to write.
   */
  void safeWriteFile(String filename, String data) {
    logger.info("Write to a file");
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

  /**
   * Appends the name and details of runway to a file, handling all errors.
   *
   * @param filename File to append to
   * @param data Data of the runway
   */
  void safeAppendFile(String filename, String data) {
    logger.info("Append to a file");
    try {
      FileWriter myWriter = new FileWriter(filename, true);
      String runwayName = App.runway.getName();
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();

      myWriter.append("RUNWAY NAME:").append(runwayName).append(" TIME:").append(dtf.format(now));
      if (isTakeoff) {
        myWriter.append(" TAKEOFF ");
      } else {
        myWriter.append(" LANDING ");
      }

      if (isAwayOver) {
        myWriter.append("AWAY/OVER");
      } else {
        myWriter.append("TOWARD/AWAY");
      }
      myWriter.append(String.valueOf('\n'));
      myWriter.append(data);
      myWriter.append('\n');
      myWriter.append('\n');
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  /**
   * Outputs all the calculations made by the system to a file called <code>calculations.txt</code>.
   */
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

    int slopeCalc = App.obstruction.getHeight() * 50;

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
              slopeCalc,
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
              slopeCalc,
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
      logger.info("Saving the calculations to the file");
      File myObj = new File("calculations.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
      infoBox("You created a file with all the calculations", "Calculations");
      safeWriteFile("calculations.txt", calculationsString);


    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }


  /**
   * Automatically outputs all the calculations made by the system to a file called
   * <code>calculationhistory.txt</code>.
   */
  public void recordFile() {
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

    int slopeCalc = App.obstruction.getHeight() * 50;

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
              slopeCalc,
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
              slopeCalc,
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
      logger.info("Saving the calculations to the file");
      File myObj = new File("calculationhistory.txt");
      if (myObj.exists()) {
        System.out.println("File exists: " + myObj.getName());
      } else {
        System.out.println("File does not exist.");
        myObj.createNewFile();
      }
      safeAppendFile("calculationhistory.txt", calculationsString);


    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  /**
   * Update the onscreen values for the runway, shown above the canvas.
   */
  @FXML
  public void setLabel() {
    logger.info("Set the new values of the Runway");
    lda.setText(String.valueOf(App.runway.getLda()));
    tora.setText(String.valueOf(App.runway.getTora()));
    asda.setText(String.valueOf(App.runway.getAsda()));
    toda.setText(String.valueOf(App.runway.getToda()));
    threshold.setText(String.valueOf(App.runway.getDisplacedThreshold()));
    slopeCalculation.setText(String.valueOf(App.obstruction.getHeight() * 50));
    if (menu.getText().equals("Landing/Takeoff")) {
      // This is only run on startup on this page
      var stage = (Stage) toggleToraButton.getScene().getWindow();
      if (origHeight == -1) {
        origHeight = stage.getHeight();
      }
      stage.setHeight(825);
      toggleToraButton.setDisable(true);
      toggleTodaButton.setDisable(true);
      toggleAsdaButton.setDisable(true);
      toggleResaButton.setDisable(true);
      toggleSeButton.setDisable(true);
      toggleH50Button.setDisable(true);
      toggleEbaButton.setDisable(true);
      toggleHButton.setDisable(true);
      toggleLdaButton.setDisable(true);
      drawBlankCanvases();
    }
  }

  /**
   * Return to the runway input screen.
   *
   * @throws IOException If the screen cannot be changed.
   */
  public void newRunway() throws IOException {
    recordFile();
    var stage = (Stage) toggleToraButton.getScene().getWindow();
    stage.setHeight(origHeight);
    App.setRoot("Input");
    infoBox("You have finished working on this runway going back to the beginning", "Going back");
  }

  /**
   * Draws default canvases asking the user to select either landing or takeoff.
   */
  private void drawBlankCanvases() {
    GraphicsContext gc = sideViewCanvas.getGraphicsContext2D();
    gc.setFill(skyBlue);
    gc.fillRect(0, 0, sideViewCanvas.getWidth(), sideViewCanvas.getHeight());
    gc.setFill(Color.WHITE);
    gc.fillText("Select either landing or takeoff to continue",
        sideViewCanvas.getWidth() / 2 - 150, sideViewCanvas.getHeight() / 2);

    gc = topViewCanvas.getGraphicsContext2D();
    gc.setFill(grassGreen);
    gc.fillRect(0, 0, topViewCanvas.getWidth(), topViewCanvas.getHeight());
    gc.setFill(Color.WHITE);
    gc.fillText("Select either landing or takeoff to continue",
        topViewCanvas.getWidth() / 2 - 150, topViewCanvas.getHeight() / 2);
  }

  /**
   * Draws both top and side view.
   */
  private void drawBothViews() {
    if (isTakeoff && isAwayOver) {
      // TOA
      toggleToraButton.setDisable(false);
      toggleTodaButton.setDisable(false);
      toggleAsdaButton.setDisable(false);
      toggleResaButton.setDisable(true);
      toggleSeButton.setDisable(true);
      toggleH50Button.setDisable(true);
      toggleEbaButton.setDisable(false);
      toggleHButton.setDisable(false);
      toggleLdaButton.setDisable(true);
    } else if (isTakeoff) {
      // TOT
      toggleToraButton.setDisable(false);
      toggleTodaButton.setDisable(false);
      toggleAsdaButton.setDisable(false);
      toggleResaButton.setDisable(false);
      toggleSeButton.setDisable(false);
      toggleH50Button.setDisable(false);
      toggleEbaButton.setDisable(true);
      toggleHButton.setDisable(false);
      toggleLdaButton.setDisable(true);
    } else if (isAwayOver) {
      // LO
      toggleToraButton.setDisable(true);
      toggleTodaButton.setDisable(true);
      toggleAsdaButton.setDisable(true);
      toggleResaButton.setDisable(false);
      toggleSeButton.setDisable(false);
      toggleH50Button.setDisable(false);
      toggleEbaButton.setDisable(true);
      toggleHButton.setDisable(false);
      toggleLdaButton.setDisable(false);
    } else {
      // LT
      toggleToraButton.setDisable(true);
      toggleTodaButton.setDisable(true);
      toggleAsdaButton.setDisable(true);
      toggleResaButton.setDisable(false);
      toggleSeButton.setDisable(false);
      toggleH50Button.setDisable(true);
      toggleEbaButton.setDisable(true);
      toggleHButton.setDisable(false);
      toggleLdaButton.setDisable(false);
    }
    var rName = new RunwayName(App.runway.getName());
    System.out.println(rName.getName());
    System.out.println(rName.getReverseName());
    drawTopView(rName.getHeading(), rName);
    drawSideView(rName);
  }

  /**
   * Draws the top, crow's eye view of the runway. Uses a JavaFX canvas and deals with the devil.
   *
   * @param degrees The angle of rotation of the runway in degrees.
   * @param name The name of the runway to use as a label.
   */
  private void drawTopView(double degrees, RunwayName name) {
    degrees += 90;
    var textUpsideDown = !(degrees < 90 || degrees > 270);
    // Drawing stuff
    GraphicsContext gc = topViewCanvas.getGraphicsContext2D();
    double width = topViewCanvas.getWidth();
    double height = topViewCanvas.getHeight();
    gc.translate(width / 2, height / 2);
    gc.rotate(degrees);
    gc.translate(-width / 2, -height / 2);

    // Grass
    gc.setFill(grassGreen);
    gc.fillRect(-20, -20, width + 20, height + 20);

    // Some positions
    double threshold = 0;
    double displacedThreshold = threshold + App.runway.getDisplacedThreshold();
    double originalRunwayLength = App.runway.getOriginalTora() + App.runway.getStripEnd();
    double obstacleX = displacedThreshold + App.obstruction.getDistanceFromThresh();
    double obstacleY = App.obstruction.getDistanceFromCl();

    double runwayPadding = 30;
    double runwayWidth = 30;
    double runwayEndX = width - runwayPadding;
    double scaleFactor = (runwayEndX - runwayPadding) / originalRunwayLength;
    double runwayYTop = height / 2 - runwayWidth / 2;
    double runwayYBottom = height / 2 + runwayWidth / 2;
    var pcc = new PixelCoordinateConverter(-scaleFactor, runwayEndX);

    // Cleared and graded

    var cgc60 = 10;
    var cgc150 = 10;
    var cgc300 = 20;
    var cgc75v = 10;
    var cgc105v = 20;
    var cgc150v = 40;
    gc.setFill(Color.PURPLE);
      {
      var x1 = runwayPadding - cgc60;
      var y1 = runwayYTop - cgc150v;
      var x2 = runwayEndX + cgc60;
      var y2 = runwayYBottom + cgc150v;
      gc.fillRect(x1, y1, x2 - x1, y2 - y1);
      }
    gc.setFill(Color.BLUE);
    gc.fillPolygon(
        new double[] {
            runwayPadding - cgc60, runwayPadding + cgc150, runwayPadding + cgc300,
            runwayEndX - cgc300, runwayEndX - cgc150, runwayEndX + cgc60,
            runwayEndX + cgc60, runwayEndX - cgc150, runwayEndX - cgc300,
            runwayPadding + cgc300, runwayPadding + cgc150, runwayPadding - cgc60},
        new double[] {
            runwayYTop - cgc75v, runwayYTop - cgc75v, runwayYTop - cgc105v, runwayYTop - cgc105v,
            runwayYTop - cgc75v, runwayYTop - cgc75v, runwayYBottom + cgc75v,
            runwayYBottom + cgc75v, runwayYBottom + cgc105v, runwayYBottom + cgc105v,
            runwayYBottom + cgc75v, runwayYBottom + cgc75v},
        12);

    // Runway
    gc.setFill(asphaltGrey);
    gc.fillRect(runwayPadding, runwayYTop, width - (runwayPadding * 2), runwayWidth);
    gc.setStroke(Color.WHITE);
    gc.setLineDashes(7);
    gc.strokeLine(runwayPadding, height / 2, width - runwayPadding, height / 2);
    gc.setLineDashes();
    gc.setFill(Color.WHITE);
      {
      // This runway text
      var ypos = height / 2;
      gc.translate(runwayEndX, ypos);
      gc.rotate(-90);
      gc.fillText(name.getName(), -10, 15);
      gc.rotate(90);
      gc.translate(-runwayEndX, -ypos);
      }
      {
      // Opposing runway text
      var ypos = height / 2;
      gc.translate(runwayPadding, ypos);
      gc.rotate(90);
      gc.fillText(name.getReverseName(), -10, 15);
      gc.rotate(-90);
      gc.translate(-runwayPadding, -ypos);
      }
    drawArrow(gc, runwayEndX - 150, height - 70, true);


    // Thresholds
    if (threshold == displacedThreshold) {
      gc.setFill(Color.BLACK);
      gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayWidth);
    } else {
      gc.setFill(Color.WHITE);
      gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayWidth);
      gc.setFill(Color.BLACK);
      gc.fillRect(pcc.conv(displacedThreshold) - 2, runwayYTop, 4, runwayWidth);
    }

    // Obstacle
    gc.setFill(Color.RED);
    gc.fillOval(pcc.conv(obstacleX) - 2.5, obstacleY * scaleFactor + (height / 2) - 2.5, 5.0, 5.0);

    String mode;
    if (isTakeoff && isAwayOver) {
      mode = "TOA";
    } else if (isTakeoff) {
      mode = "TOT";
    } else if (isAwayOver) {
      mode = "LO";
    } else {
      mode = "LT";
    }

    var labelYPos = runwayYTop - 15;
    var labelOppositeYPos = runwayYTop + runwayWidth + 15;
    String toraString = Integer.toString(App.runway.getTora());
    String todaString = Integer.toString(App.runway.getToda());
    String asdaString = Integer.toString(App.runway.getAsda());
    String ldaString = Integer.toString(App.runway.getLda());
    String resaString = Integer.toString(App.runway.getResa());
    String heightCalcString = Integer.toString(App.obstruction.getHeight() * 50);
    String ebaString = "300";
    String seString = Integer.toString(App.runway.getStripEnd());

    switch (mode) {
      case "TOA" -> {
        // Positions
        var ebaEnd = displacedThreshold + 300;

        var toraEnd = ebaEnd + App.runway.getTora();
        var todaEnd = ebaEnd + App.runway.getToda();
        var asdaEnd = ebaEnd + App.runway.getAsda();
        var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;

        drawHorizontalBarBetween(gc, pcc.conv(ebaEnd), labelOppositeYPos,
            pcc.conv(displacedThreshold), ebaString + "m (EBA)", false, textUpsideDown);
        if (toraEnd == todaEnd && todaEnd == asdaEnd && (showTora && showToda
            && showAsda)) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(ebaEnd),
              toraString + "m (TORA,TODA,ASDA)", true, textUpsideDown);
        } else {
          if (showTora) {
            drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(ebaEnd),
                toraString + "m (TORA)", true, textUpsideDown);
          }
          if (showToda) {
            drawHorizontalBarBetween(gc, pcc.conv(todaEnd), labelYPos - 20,
                pcc.conv(ebaEnd), todaString + "m (TODA)", true, textUpsideDown);
          }
          if (showAsda) {
            drawHorizontalBarBetween(gc, pcc.conv(asdaEnd), labelYPos - 40,
                pcc.conv(ebaEnd), asdaString + "m (ASDA)", true, textUpsideDown);
          }
        }
        if (asdaEnd != toraEnd) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(asdaEnd),
              (int) (asdaEnd - toraEnd) + "m (STP/CLR)", false, false);
        }
      }
      case "LO" -> {
        var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;
        double heightCalcStart;
        double resaStart;
        double heightCalcResaEnd;
        if (App.obstruction.getHeight() * 50 > App.runway.getResa()) {
          heightCalcStart = obstacle;
          heightCalcResaEnd = obstacle + App.obstruction.getHeight() * 50;
          resaStart = heightCalcResaEnd - App.runway.getResa();
        } else {
          resaStart = obstacle;
          heightCalcResaEnd = obstacle + App.runway.getResa();
          heightCalcStart = heightCalcResaEnd - App.obstruction.getHeight() * 50;
        }
        var stripEndEnd = heightCalcResaEnd + App.runway.getStripEnd();
        var ldaEnd = stripEndEnd + App.runway.getLda();

        if (showH50) {
          drawHorizontalBarBetween(gc, pcc.conv(heightCalcResaEnd), labelOppositeYPos,
              pcc.conv(heightCalcStart), heightCalcString + "m (hx50)", false,
              textUpsideDown);
        }
        if (showResa) {
          drawHorizontalBarBetween(gc, pcc.conv(heightCalcResaEnd), labelYPos,
              pcc.conv(resaStart), resaString + "m (RESA)", true, textUpsideDown);
        }
        if (showSe) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos,
              pcc.conv(heightCalcResaEnd), seString + "m (SE)", false, textUpsideDown);
        }
        if (showLDA) {
          drawHorizontalBarBetween(gc, pcc.conv(ldaEnd), labelYPos, pcc.conv(stripEndEnd),
              ldaString + "m (LDA)", true, textUpsideDown);
        }
      }
      case "TOT" -> {
        // Positions
        var toraEnd = displacedThreshold + App.runway.getTora();
        var todaEnd = displacedThreshold + App.runway.getToda();
        var asdaEnd = displacedThreshold + App.runway.getAsda();
        var stripEndEnd = toraEnd + App.runway.getStripEnd();
        var resaEnd = stripEndEnd + App.runway.getResa();
        var heightCalcEnd = stripEndEnd + App.obstruction.getHeight() * 50;

        // Labels
        if (toraEnd == todaEnd && todaEnd == asdaEnd && (showTora && showToda
            && showAsda)) {
          drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos,
              pcc.conv(toraEnd), toraString + "m (TORA,TODA,ASDA)", true, textUpsideDown);
        } else {
          if (showTora) {
            drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos,
                pcc.conv(toraEnd), toraString + "m (TORA)", true, textUpsideDown);
          }
          if (showToda) {
            drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos - 20,
                pcc.conv(todaEnd), todaString + "m (TODA)", true, textUpsideDown);
          }
          if (showAsda) {
            drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos - 40,
                pcc.conv(asdaEnd), asdaString + "m (ASDA)", true, textUpsideDown);
          }
        }
        if (showSe) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos,
              pcc.conv(stripEndEnd), seString + "m (SE)", false, textUpsideDown);
        }
        if (showResa) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos,
              pcc.conv(resaEnd), resaString + "m (RESA)", true, textUpsideDown);
        }
        if (showH50) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos + 43,
              pcc.conv(heightCalcEnd), heightCalcString + "m (hx50)", false,
              textUpsideDown);
        }
        if (asdaEnd != toraEnd) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(asdaEnd),
              (int) (asdaEnd - toraEnd) + "m (STOP/CLEAR)", false, false);
        }
      }
      default -> {
        // Positions
        var ldaEnd = displacedThreshold + App.runway.getLda();
        var stripEndEnd = ldaEnd + App.runway.getStripEnd();
        var resaEnd = stripEndEnd + App.runway.getResa();

        // Labels
        if (showLDA) {
          drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos,
              pcc.conv(ldaEnd), ldaString + "m (LDA)", true, textUpsideDown);
        }
        if (showSe) {
          drawHorizontalBarBetween(gc, pcc.conv(ldaEnd), labelYPos, pcc.conv(stripEndEnd),
              seString + "m (SE)", false, textUpsideDown);
        }
        if (showResa) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos,
              pcc.conv(resaEnd), resaString + "m (RESA)", true, textUpsideDown);
        }
      }
    }


    gc.translate(width / 2, height / 2);
    gc.rotate(-degrees);
    gc.translate(-width / 2, -height / 2);
  }

  /**
   * Draws the side view of the runway. Uses a JavaFX canvas and dark magic unknown to human beings.
   *
   * @param name The name of the runway to use as a label.
   */
  private void drawSideView(RunwayName name) {
    // Drawing stuff
    GraphicsContext gc = sideViewCanvas.getGraphicsContext2D();
    double width = sideViewCanvas.getWidth();
    double height = sideViewCanvas.getHeight();

    double runwayYTop = height / 2.5;
    double runwayDepth = 10;

    double runwayPadding = 30;

    double runwayEndX = width - runwayPadding;
    double originalRunwayLength = App.runway.getOriginalTora() + App.runway.getStripEnd();
    double scaleFactor = (runwayEndX - runwayPadding) / originalRunwayLength;

    double verticalExtraScaleFactor = 10;
    double obstaclePixelHeight = App.obstruction.getHeight() * scaleFactor
        * verticalExtraScaleFactor;
    // Grass
    gc.setFill(grassGreen);
    gc.fillRect(0, runwayYTop, width, height - runwayYTop);

    // Sky
    gc.setFill(skyBlue);
    gc.fillRect(0, 0, width, runwayYTop);

    // Runway
    gc.setFill(asphaltGrey);
    gc.fillRect(runwayPadding, runwayYTop, width - (runwayPadding * 2), runwayDepth);

    String mode;
    if (isTakeoff && isAwayOver) {
      mode = "TOA";
    } else if (isTakeoff) {
      mode = "TOT";
    } else if (isAwayOver) {
      mode = "LO";
    } else {
      mode = "LT";
    }

    var labelYPos = runwayYTop + runwayDepth + 20;
    String toraString = Integer.toString(App.runway.getTora());
    String todaString = Integer.toString(App.runway.getToda());
    String asdaString = Integer.toString(App.runway.getAsda());
    String ldaString = Integer.toString(App.runway.getLda());
    String resaString = Integer.toString(App.runway.getResa());
    String heightCalcString = Integer.toString(App.obstruction.getHeight() * 50);
    String ebaString = "300";
    String seString = Integer.toString(App.runway.getStripEnd());

    switch (mode) {
      case "TOT" -> {
        // Positions
        var threshold = 0;
        var displacedThreshold = threshold + App.runway.getDisplacedThreshold();
        var toraEnd = displacedThreshold + App.runway.getTora();
        var todaEnd = displacedThreshold + App.runway.getToda();
        var asdaEnd = displacedThreshold + App.runway.getAsda();
        var stripEndEnd = toraEnd + App.runway.getStripEnd();
        var resaEnd = stripEndEnd + App.runway.getResa();
        var heightCalcEnd = stripEndEnd + App.obstruction.getHeight() * 50;
        var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;
        var pcc = new PixelCoordinateConverter(scaleFactor, runwayPadding);

        // Thresholds
        if (threshold == displacedThreshold) {
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
        } else {
          gc.setFill(Color.WHITE);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(displacedThreshold) - 2, runwayYTop, 4, runwayDepth);
        }

        // Obstacle
        gc.setFill(Color.RED);
        gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5,
            obstaclePixelHeight);
        if (showH) {
          drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight,
              obstaclePixelHeight, " " + App.obstruction.getHeight()
                  + "m", true, false);
        }

        // Labels
        if (toraEnd == todaEnd && todaEnd == asdaEnd && (showTora && showToda
            && showAsda)) {
          drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos,
              pcc.conv(toraEnd), toraString + "m (TORA,TODA,ASDA)", true, false);
        } else {
          if (showTora) {
            drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos,
                pcc.conv(toraEnd), toraString + "m (TORA)", true, false);
          }
          if (showToda) {
            drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos + 20,
                pcc.conv(todaEnd), todaString + "m (TODA)", true, false);
          }
          if (showAsda) {
            drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos + 40,
                pcc.conv(asdaEnd), asdaString + "m (ASDA)", true, false);
          }
        }
        if (showSe) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos,
              pcc.conv(stripEndEnd), seString + "m (SE)", false, false);
        }
        if (showResa) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos,
              pcc.conv(resaEnd), resaString + "m (RESA)", true, false);
        }
        if (showH50) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos + 43,
              pcc.conv(heightCalcEnd), heightCalcString + "m (hx50)", true, false);
        }
        if (showH50) {
          gc.setStroke(Color.RED);
          gc.setLineWidth(1);
          var xd = pcc.conv(toraEnd) - pcc.conv(stripEndEnd);
          gc.strokeLine(pcc.conv(stripEndEnd), runwayYTop, pcc.conv(heightCalcEnd),
              runwayYTop - obstaclePixelHeight);
          gc.setStroke(Color.BLACK);
          gc.strokeLine(pcc.conv(stripEndEnd) + xd, runwayYTop,
              pcc.conv(heightCalcEnd) + xd, runwayYTop - obstaclePixelHeight);
        }
        if (asdaEnd != toraEnd) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(asdaEnd),
              asdaEnd - toraEnd + "m (STOP/CLEAR)", true, false);
        }
        gc.fillText("Takeoff from left to right", 30, 30);
        drawArrow(gc, 30, 35, false);
      }
      case "LT" -> {
        // Positions
        var threshold = 0;
        var displacedThreshold = threshold + App.runway.getDisplacedThreshold();
        var ldaEnd = displacedThreshold + App.runway.getLda();
        var stripEndEnd = ldaEnd + App.runway.getStripEnd();
        var resaEnd = stripEndEnd + App.runway.getResa();
        var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;
        var pcc = new PixelCoordinateConverter(scaleFactor, runwayPadding);

        // Thresholds
        if (threshold == displacedThreshold) {
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
        } else {
          gc.setFill(Color.WHITE);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(displacedThreshold) - 2, runwayYTop, 4, runwayDepth);
        }

        // Obstacle
        gc.setFill(Color.RED);
        gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5,
            obstaclePixelHeight);
        if (showH) {
          drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight,
              obstaclePixelHeight, " " + App.obstruction.getHeight()
                  + "m", true, false);
        }

        // Labels
        if (showLDA) {
          drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos,
              pcc.conv(ldaEnd), ldaString + "m (LDA)", true, false);
        }
        if (showSe) {
          drawHorizontalBarBetween(gc, pcc.conv(ldaEnd), labelYPos, pcc.conv(stripEndEnd),
              seString + "m (SE)", false, false);
        }
        if (showResa) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos,
              pcc.conv(resaEnd), resaString + "m (RESA)", true, false);
        }
        gc.fillText("Landing from left to right", 30, 30);
        drawArrow(gc, 30, 35, false);
      }
      case "TOA" -> {
        // Positions
        var threshold = 0;
        var displacedThreshold = threshold + App.runway.getDisplacedThreshold();
        var ebaEnd = displacedThreshold + 300;

        var toraEnd = ebaEnd + App.runway.getTora();
        var todaEnd = ebaEnd + App.runway.getToda();
        var asdaEnd = ebaEnd + App.runway.getAsda();
        var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;
        var pcc = new PixelCoordinateConverter(-scaleFactor, runwayEndX);

        // Thresholds
        if (threshold == displacedThreshold) {
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
        } else {
          gc.setFill(Color.WHITE);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(displacedThreshold) - 2, runwayYTop, 4, runwayDepth);
        }

        // Obstacle
        gc.setFill(Color.RED);
        gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5,
            obstaclePixelHeight);
        if (showH) {
          drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight,
              obstaclePixelHeight, " " + App.obstruction.getHeight()
                  + "m", true, false);
        }

        // Labels
        if (showEba) {
          drawHorizontalBarBetween(gc, pcc.conv(ebaEnd), labelYPos,
              pcc.conv(displacedThreshold), ebaString + "m (EBA)", true, false);
        }
        if (toraEnd == todaEnd && todaEnd == asdaEnd && (showTora && showToda
            && showAsda)) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(ebaEnd),
              toraString + "m (TORA,TODA,ASDA)", true, false);
        } else {
          if (showTora) {
            drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(ebaEnd),
                toraString + "m (TORA)", true, false);
          }
          if (showToda) {
            drawHorizontalBarBetween(gc, pcc.conv(todaEnd), labelYPos + 20,
                pcc.conv(ebaEnd), todaString + "m (TODA)", true, false);
          }
          if (showAsda) {
            drawHorizontalBarBetween(gc, pcc.conv(asdaEnd), labelYPos + 40,
                pcc.conv(ebaEnd), asdaString + "m (ASDA)", true, false);
          }
        }
        if (asdaEnd != toraEnd) {
          drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(asdaEnd),
              asdaEnd - toraEnd + "m (STOP/CLEAR)", true, false);
        }
        gc.fillText("Takeoff from right to left", 30, 30);
        drawArrow(gc, 30, 35, true);
      }
      default -> {
        // Positions
        var threshold = 0;
        var displacedThreshold = threshold + App.runway.getDisplacedThreshold();
        var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;
        double heightCalcStart;
        double resaStart;
        double heightCalcResaEnd;
        if (App.obstruction.getHeight() * 50 > App.runway.getResa()) {
          heightCalcStart = obstacle;
          heightCalcResaEnd = obstacle + App.obstruction.getHeight() * 50;
          resaStart = heightCalcResaEnd - App.runway.getResa();
        } else {
          resaStart = obstacle;
          heightCalcResaEnd = obstacle + App.runway.getResa();
          heightCalcStart = heightCalcResaEnd - App.obstruction.getHeight() * 50;
        }
        var stripEndEnd = heightCalcResaEnd + App.runway.getStripEnd();
        var ldaEnd = stripEndEnd + App.runway.getLda();
        var pcc = new PixelCoordinateConverter(-scaleFactor, runwayEndX);

        // Thresholds
        if (threshold == displacedThreshold) {
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
        } else {
          gc.setFill(Color.WHITE);
          gc.fillRect(pcc.conv(threshold) - 2, runwayYTop, 4, runwayDepth);
          gc.setFill(Color.BLACK);
          gc.fillRect(pcc.conv(displacedThreshold) - 2, runwayYTop, 4, runwayDepth);
        }
        // Obstacle
        gc.setFill(Color.RED);
        gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5,
            obstaclePixelHeight);
        if (showH) {
          drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight,
              obstaclePixelHeight, " " + App.obstruction.getHeight()
                  + "m", true, false);
        }

        // Labels
        if (showH50) {
          drawHorizontalBarBetween(gc, pcc.conv(heightCalcResaEnd), labelYPos + 45,
              pcc.conv(heightCalcStart), heightCalcString + "m (hx50)", true, false);
        }
        if (showH50) {
          gc.setStroke(Color.RED);
          gc.setLineWidth(1);
          var xd = pcc.conv(heightCalcResaEnd) - pcc.conv(stripEndEnd);
          gc.strokeLine(pcc.conv(heightCalcResaEnd), runwayYTop,
              pcc.conv(heightCalcStart), runwayYTop - obstaclePixelHeight);
          gc.setStroke(Color.BLACK);
          gc.strokeLine(pcc.conv(heightCalcResaEnd) - xd, runwayYTop,
              pcc.conv(heightCalcStart) - xd, runwayYTop - obstaclePixelHeight);
        }
        if (showResa) {
          drawHorizontalBarBetween(gc, pcc.conv(heightCalcResaEnd), labelYPos,
              pcc.conv(resaStart), resaString + "m (RESA)", true, false);
        }
        if (showSe) {
          drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos,
              pcc.conv(heightCalcResaEnd), seString + "m (SE)", false, false);
        }
        if (showLDA) {
          drawHorizontalBarBetween(gc, pcc.conv(ldaEnd), labelYPos, pcc.conv(stripEndEnd),
              ldaString + "m (LDA)", true, false);
        }
        gc.fillText("Landing from right to left", 30, 30);
        drawArrow(gc, 30, 35, true);
      }
    }
  }

  /**
   * Draw a white horizontal distance indicator with a label between two x positions.
   *
   * @param gc           The GraphicsContext to write to.
   * @param x            The x coordinate of the left most point of the line.
   * @param y            The y coordinate of the entire line.
   * @param x2           The x coordinate of the right most point of the line.
   * @param label        The label to attach to the indicator.
   * @param isAbove      Moves the text above or below the line.
   * @param isUpsideDown Rotates the text upside down.
   */
  private void drawHorizontalBarBetween(GraphicsContext gc, double x, double y, double x2,
      String label, boolean isAbove, boolean isUpsideDown) {
    drawHorizontalBar(gc, x, y, x2 - x, label, isAbove, isUpsideDown);
  }

  /**
   * Draw a white horizontal distance indicator with a label and a length.
   *
   * @param gc           The GraphicsContext to write to.
   * @param x            The x coordinate of the left most point of the line.
   * @param y            The y coordinate of the entire line.
   * @param l            The length of the line.
   * @param label        The label to attach to the indicator.
   * @param isAbove      Moves the text above or below the line.
   * @param isUpsideDown Rotates the text upside down.
   */
  private void drawHorizontalBar(GraphicsContext gc, double x, double y, double l, String label,
      boolean isAbove, boolean isUpsideDown) {
    gc.setStroke(Color.WHITE);
    gc.setLineWidth(2);
    gc.strokeLine(x, y, x + l, y);
    gc.strokeLine(x, y - 5, x, y + 5);
    gc.strokeLine(x + l, y - 5, x + l, y + 5);
    gc.setFill(Color.WHITE);
    if (!isUpsideDown) {
      var xpos = x + (l / 2) - (label.length() / 2 * 7);
      if (isAbove) {
        gc.fillText(label, xpos, y - 7);
      } else {
        gc.fillText(label, xpos, y + 17);
      }
    } else {
      var xpos = x + (l / 2) + (label.length() / 2 * 7);
      double ypos;
      if (isAbove) {
        ypos = y - 14;
      } else {
        ypos = y + 10;
      }
      gc.translate(xpos, ypos);
      gc.rotate(180);
      gc.fillText(label, 0, 0);
      gc.rotate(-180);
      gc.translate(-xpos, -ypos);
    }
  }

  /**
   * Draw a white vertical distance indicator with a label between two y positions.
   *
   * @param gc           The GraphicsContext to write to.
   * @param x            The x coordinate of the entire line.
   * @param y            The y coordinate of the highest point of the line.
   * @param y2           The y coordinate of the lowest point of the line.
   * @param label        The label to attach to the indicator.
   * @param isAbove      Moves the text above or below the line.
   * @param isUpsideDown Rotates the text upside down.
   */
  private void drawVerticalBarBetween(GraphicsContext gc, double x, double y, double y2,
      String label, boolean isAbove, boolean isUpsideDown) {
    drawVerticalBar(gc, x, y, y2 - y, label, isAbove, isUpsideDown);
  }

  /**
   * Draw a white vertical distance indicator with a label and a length.
   *
   * @param gc           The GraphicsContext to write to.
   * @param x            The x coordinate of the entire line.
   * @param y            The y coordinate of the highest point of the line.
   * @param l            The length of the line.
   * @param label        The label to attach to the indicator.
   * @param isAbove      Moves the text above or below the line.
   * @param isUpsideDown Rotates the text upside down.
   */
  private void drawVerticalBar(GraphicsContext gc, double x, double y, double l, String label,
      boolean isAbove, boolean isUpsideDown) {
    gc.setStroke(Color.WHITE);
    gc.setLineWidth(2);
    gc.strokeLine(x, y, x, y + l);
    gc.strokeLine(x - 5, y, x + 5, y);
    gc.strokeLine(x - 5, y + l, x + 5, y + l);
    gc.setFill(Color.WHITE);
    gc.fillText(label, x + 5, y + (l / 2));
  }

  /**
   * Draws a horizontal arrow facing either left or right.
   *
   * @param gc The GraphicsContext to write to.
   * @param x X position of the point of the arrow
   * @param y y position of the point of the arrow
   * @param rToL True if arrow is right to left, false otherwise
   */
  private void drawArrow(GraphicsContext gc, double x, double y, boolean rToL) {
    gc.setStroke(Color.WHITE);
    gc.setLineWidth(2);
    var x1 = x + 70;
    gc.strokeLine(x, y, x1, y);
    if (!rToL) {
      gc.fillPolygon(new double[]{x1, x1 - 5, x1 - 5}, new double[]{y, y + 5, y - 5}, 3);
    } else {
      gc.fillPolygon(new double[]{x, x + 5, x + 5}, new double[]{y, y + 5, y - 5}, 3);
    }

  }

  /**
   * Set the visualisation to be a takeoff event.
   *
   * @param actionEvent ignored
   */
  public void setTO(ActionEvent actionEvent) {
    isTakeoff = true;
    menu.setText(takeoff.getText());
    drawBothViews();
  }

  /**
   * Set the visualisation to be a landing event.
   *
   * @param actionEvent ignored
   */
  public void setL(ActionEvent actionEvent) {
    isTakeoff = false;
    menu.setText(landing.getText());
    drawBothViews();
  }

  /**
   * Toggles the visibility of the TODA label.
   *
   * @param actionEvent ignored
   */
  public void toggleToda(ActionEvent actionEvent) {
    showToda = !showToda;
    if (showToda) {
      toggleTodaButton.setText("TODA");
      toggleTodaButton.setStyle("button");
    } else {
      toggleTodaButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the TORA label.
   *
   * @param actionEvent ignored
   */
  public void toggleTora(ActionEvent actionEvent) {
    showTora = !showTora;
    if (showTora) {
      toggleToraButton.setText("TORA");
      toggleToraButton.setStyle("button");
    } else {
      toggleToraButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the ASDA label.
   *
   * @param actionEvent ignored
   */
  public void toggleAsda(ActionEvent actionEvent) {
    showAsda = !showAsda;
    if (showAsda) {
      toggleAsdaButton.setText("ASDA");
      toggleAsdaButton.setStyle("button");
    } else {
      toggleAsdaButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the RESA label.
   *
   * @param actionEvent ignored
   */
  public void toggleResa(ActionEvent actionEvent) {
    showResa = !showResa;
    if (showResa) {
      toggleResaButton.setText("RESA");
      toggleResaButton.setStyle("button");
    } else {
      toggleResaButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the SE label.
   *
   * @param actionEvent ignored
   */
  public void toggleSe(ActionEvent actionEvent) {
    showSe = !showSe;
    if (showSe) {
      toggleSeButton.setText("SE");
      toggleSeButton.setStyle("button");
    } else {
      toggleSeButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the H50 label.
   *
   * @param actionEvent ignored
   */
  public void toggleH50(ActionEvent actionEvent) {
    showH50 = !showH50;
    if (showH50) {
      toggleH50Button.setText("H50");
      toggleH50Button.setStyle("button");
    } else {
      toggleH50Button.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the EBA label.
   *
   * @param actionEvent ignored
   */
  public void toggleEba(ActionEvent actionEvent) {
    showEba = !showEba;
    if (showEba) {
      toggleEbaButton.setText("EBA");
      toggleEbaButton.setStyle("button");
    } else {
      toggleEbaButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the H label.
   *
   * @param actionEvent ignored
   */
  public void toggleH(ActionEvent actionEvent) {
    showH = !showH;
    if (showH) {
      toggleHButton.setText("H");
      toggleHButton.setStyle("button");
    } else {
      toggleHButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles the visibility of the LDA label.
   *
   * @param actionEvent ignored
   */
  public void toggleLda(ActionEvent actionEvent) {
    showLDA = !showLDA;
    if (showLDA) {
      toggleLdaButton.setText("LDA");
      toggleLdaButton.setStyle("button");
    } else {
      toggleLdaButton.setStyle("-fx-background-color: #ff0000; ");
    }
    drawBothViews();
  }

  /**
   * Toggles high contrast mode.
   *
   * @author snow6701
   */
  public void changeContrast() {
    if (highContrast) {
      highContrast = false;
      calculation.getStyleClass().clear();
      goback.getStyleClass().clear();
      contrastB.getStyleClass().clear();
      toggleToraButton.getStyleClass().clear();
      toggleTodaButton.getStyleClass().clear();
      toggleAsdaButton.getStyleClass().clear();
      toggleResaButton.getStyleClass().clear();
      toggleSeButton.getStyleClass().clear();
      toggleH50Button.getStyleClass().clear();
      toggleEbaButton.getStyleClass().clear();
      toggleHButton.getStyleClass().clear();
      toggleLdaButton.getStyleClass().clear();
      calculation.getStyleClass().add("button");
      goback.getStyleClass().add("button");
      contrastB.getStyleClass().add("button");
      toggleToraButton.getStyleClass().add("button");
      toggleTodaButton.getStyleClass().add("button");
      toggleAsdaButton.getStyleClass().add("button");
      toggleResaButton.getStyleClass().add("button");
      toggleSeButton.getStyleClass().add("button");
      toggleH50Button.getStyleClass().add("button");
      toggleEbaButton.getStyleClass().add("button");
      toggleHButton.getStyleClass().add("button");
      toggleLdaButton.getStyleClass().add("button");

    } else {
      highContrast = true;
      calculation.getStyleClass().add("button2");
      goback.getStyleClass().add("button2");
      contrastB.getStyleClass().add("button2");
      toggleToraButton.getStyleClass().add("button2");
      toggleTodaButton.getStyleClass().add("button2");
      toggleAsdaButton.getStyleClass().add("button2");
      toggleResaButton.getStyleClass().add("button2");
      toggleSeButton.getStyleClass().add("button2");
      toggleH50Button.getStyleClass().add("button2");
      toggleEbaButton.getStyleClass().add("button2");
      toggleHButton.getStyleClass().add("button2");
      toggleLdaButton.getStyleClass().add("button2");
    }

  }

  /**
   * Shows the manual.
   *
   * @author mccaw12
   */
  @FXML
  public void showManual() {
    infoBox("The numbers displayed are the results of the calculation... Press 'View Calculation' to see the calculation steps in a text file",
        "Help");
  }

  /**
   * Hides the manual.
   *
   * @author mccaw12
   */
  @FXML
  public void hideManual() {
    manual.setVisible(false);
  }

  /**
   * Listens for a return key keycode, and continues to the next panel (first panel) if found.
   *
   * @param event Keycode that occurred
   * @author mccaw12
   */
  public void keyListener(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER) {
      newRunway();
    }
  }

  /**
   * Converts between pixel coordinates and logical coordinates.
   *
   * @author JoshPattman
   */
  private static class PixelCoordinateConverter {
    final double scaleFactor;
    final double offset;

    public PixelCoordinateConverter(double scaleFactor, double offset) {
      this.scaleFactor = scaleFactor;
      this.offset = offset;
    }

    public double conv(double x) {
      return x * scaleFactor + offset;
    }
  }

  /**
   * Stores the data related to a runway name.
   *
   * @author JoshPattman
   */
  private class RunwayName {
    private final String des;
    private int points;

    /**
     * Parses a runway name.
     *
     * @param s name to parse
     */
    public RunwayName(String s) {
      var pointsS = "";
      if (s.length() == 2) {
        pointsS = s;
        des = "";
      } else {
        pointsS = s.substring(0, 2);
        des = s.substring(2);
      }
      try {
        points = Integer.parseInt(pointsS);
      } catch (Exception e) {
        points = 0;
      }

    }

    /**
     * Calculates and returns heading of runway.
     *
     * @return Heading of runway in degrees
     */
    public float getHeading() {
      return ((float) points * 10) % 360;
    }

    /**
     * Gets the name of the runway.
     *
     * @return The name of the runway
     */
    public String getName() {
      var ps = Integer.toString(points);
      if (ps.length() < 2) {
        ps = "0" + ps;
      }
      return ps + des;
    }

    /**
     * Calculates and returns the reverse heading of runway.
     *
     * @return Reverse heading of runway in degrees
     */
    public float getReverseHeading() {
      return ((float) points * 10 + 180) % 360;
    }

    /**
     * Gets the reverse name of the runway.
     *
     * @return The reverse name of the runway
     */
    public String getReverseName() {
      var ps = Integer.toString((points + 18) % 36);
      if (ps.length() < 2) {
        ps = "0" + ps;
      }
      return ps + flipDes(des);
    }

    /**
     * Inverts the DES of the runway.
     *
     * @param des the DES to flip
     * @return the new DES
     */
    private String flipDes(String des) {
      return switch (des) {
        case "L" -> "R";
        case "R" -> "L";
        default -> des;
      };
    }
  }
}