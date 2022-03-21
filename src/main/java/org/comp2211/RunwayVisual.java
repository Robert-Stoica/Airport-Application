package org.comp2211;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  @FXML private Label tora;
  @FXML private Label toda;
  @FXML private Label asda;
  @FXML private Label lda;
  @FXML private Canvas canvas;
  @FXML private MenuButton menu;
  @FXML private MenuItem landing;
  @FXML private MenuItem takeoff;

  Color DarkGreen = Color.color(51/255.0, 204/255.0, 51/255.0);
  Color Purple = Color.color(153/255.0, 0/255.0, 255/255.0);
  Color DarkBlue = Color.color(51/255.0, 51/255.0, 204/255.0);
  Color SkyBlue = Color.color(85/255.0, 216/255.0, 255/255.0);
  Color AsphaltGrey = Color.color(150/255.0, 150/255.0, 150/255.0);

  boolean isTakeoff;

  private static final Logger logger = LogManager.getLogger(Calculations.class);

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
        logger.info("Saving the calculations to the file");
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
      logger.info("Set the new values of the Runway");
    lda.setText(String.valueOf(App.runway.getLda()));
    tora.setText(String.valueOf(App.runway.getTora()));
    asda.setText(String.valueOf(App.runway.getAsda()));
    toda.setText(String.valueOf(App.runway.getToda()));
    //drawSideView();
  }

  public void newRunway() throws IOException {
    App.setRoot("Input");
  }

  private void drawTopView() {
    // Drawing stuff
    GraphicsContext gc = canvas.getGraphicsContext2D();
    // Grass
    gc.setFill(DarkGreen);
    gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    // Purple area
    var purpleLengthPadding = 20;
    var purpleWidthPadding = 10;
    gc.setFill(Purple);
    gc.fillRect(purpleLengthPadding,purpleWidthPadding,canvas.getWidth()-purpleLengthPadding*2,canvas.getHeight()-purpleWidthPadding*2);
    // Blue area
    gc.setFill(DarkBlue);
    double dist60 = 40;
    double distShort150 = 30;
    double distShort300 = distShort150*2;
    double dist75 = 50;

    double startPointX = purpleLengthPadding;
    double startPointY = canvas.getHeight()/2 - dist75;
    double width = canvas.getWidth();
    double height = canvas.getHeight();

    gc.fillPolygon(new double[]{
            startPointX, startPointX+dist60+distShort150, startPointX+dist60+distShort300,
            width-startPointX-dist60-distShort300, width-startPointX-dist60-distShort150,width-startPointX,
            width-startPointX,width-startPointX-dist60-distShort150,width-startPointX-dist60-distShort300,
            startPointX+dist60+distShort300,startPointX+dist60+distShort150,startPointX
    }, new double[]{
            startPointY, startPointY, startPointY-distShort150,
            startPointY-distShort150, startPointY, startPointY,
            height-startPointY, height-startPointY, height-startPointY+distShort150,
            height-startPointY+distShort150, height-startPointY, height-startPointY
    }, 12);

    //Runway
    gc.setFill(AsphaltGrey);
    double runwayWidth = 40;
    gc.fillRect(startPointX + dist60, startPointY+dist75 - (runwayWidth/2), (width-startPointX - dist60)-(startPointX + dist60), (startPointY+dist75 + (runwayWidth/2))-(startPointY+dist75 - (runwayWidth/2)));
    // Everything has been drawn, now draw distances
    drawHorizontalBar(gc,startPointX, height/2+30, dist60, 60);
    drawHorizontalBar(gc,startPointX+dist60, height/2 + 50, distShort150, 150);
    drawHorizontalBar(gc,startPointX+dist60, height/2 + 75, distShort300, 300);
    drawVerticalBar(gc, startPointX+dist60+distShort150, startPointY, (height/2) - startPointY, 75);
    drawVerticalBar(gc, width/2, (height/2), (height/2) - purpleWidthPadding, 150);
    gc.setFill(Color.WHITE);
    gc.fillText("Not to scale", 5, 9);
  }

  private void drawSideView(){
    // Drawing stuff
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double width = canvas.getWidth();
    double height = canvas.getHeight();

    double runwayYTop = height/1.5;
    double runwayDepth = 10;

    double runwayPadding = 30;

    double runwayStartX = runwayPadding;
    double runwayEndX = width - runwayPadding;
    double originalRunwayLength = App.runway.getOriginalTora() + 60;
    double scaleFactor = (runwayEndX-runwayStartX)/originalRunwayLength;

    double verticalExtraScaleFactor = 10;
    double obstacleHeight = App.obstruction.getHeight()*scaleFactor*verticalExtraScaleFactor;
    // Grass
    gc.setFill(DarkGreen);
    gc.fillRect(0,runwayYTop, width, height-runwayYTop);

    // Sky
    gc.setFill(SkyBlue);
    gc.fillRect(0,0, width, runwayYTop);

    // Runway
    gc.setFill(AsphaltGrey);
    gc.fillRect(runwayPadding,runwayYTop, width-(runwayPadding*2), runwayDepth);

    String mode= "";
    if (isTakeoff && isAwayOver){
      mode = "TOA";
    } else if (isTakeoff){
      mode = "TOT";
    } else if (isAwayOver){
      mode = "LO";
    } else{
      mode = "LT";
    }

    var labelYPos = runwayYTop + runwayDepth + 20;

    var resa = App.runway.getResa();
    var tora = App.runway.getTora();
    var toda = App.runway.getAsda();
    var asda = App.runway.getAsda();
    var lda = App.runway.getLda();

    var h50 = App.obstruction.getHeight() * 50;
    double oStartX;
    switch (mode){
      case "TOT":
        // Obstacle
        gc.setFill(Color.RED);
        oStartX = runwayStartX+(App.obstruction.getDistanceFromThresh()*scaleFactor);
        gc.fillRect(oStartX, runwayYTop-obstacleHeight, 5, obstacleHeight);
        drawVerticalBar(gc, oStartX+15, runwayYTop-obstacleHeight, obstacleHeight, " "+Integer.toString(App.obstruction.getHeight()) + "m");
        // Labels
        if (tora == toda && tora == asda){
          drawHorizontalBar(gc, runwayStartX, labelYPos, tora*scaleFactor, Integer.toString(tora)+"m (TORA,TODA,ASDA)");
        } else{
          drawHorizontalBar(gc, runwayStartX, labelYPos, tora*scaleFactor, Integer.toString(tora)+"m (TORA)");
          drawHorizontalBar(gc, runwayStartX, labelYPos+20, toda*scaleFactor, Integer.toString(toda)+"m (TODA)");
          drawHorizontalBar(gc, runwayStartX, labelYPos+40, asda*scaleFactor, Integer.toString(asda)+"m (ASDA)");
        }
        drawHorizontalBar(gc, runwayStartX+tora*scaleFactor, labelYPos, 60*scaleFactor, "\n\n"+Integer.toString(60)+"m (60)");
        drawHorizontalBar(gc, runwayStartX+(tora+60)*scaleFactor, labelYPos, resa*scaleFactor, Integer.toString(resa)+"m (RESA)");
        drawHorizontalBar(gc, oStartX-(h50*scaleFactor), labelYPos+43, h50*scaleFactor, Integer.toString(h50)+"m (hx50)");
        gc.strokeLine(oStartX-(h50*scaleFactor), runwayYTop, oStartX, runwayYTop-obstacleHeight);
        gc.strokeLine(oStartX-(h50*scaleFactor-60*scaleFactor), runwayYTop, oStartX-60*scaleFactor, runwayYTop-obstacleHeight);
        gc.fillText("Takeoff from left to right", 30,30);
        break;
      case "LT":
        // Obstacle
        gc.setFill(Color.RED);
        oStartX = runwayStartX+(App.obstruction.getDistanceFromThresh()*scaleFactor);
        gc.fillRect(oStartX, runwayYTop-obstacleHeight, 5, obstacleHeight);
        drawVerticalBar(gc, oStartX+15, runwayYTop-obstacleHeight, obstacleHeight, " "+Integer.toString(App.obstruction.getHeight()) + "m");
        // Labels
        drawHorizontalBar(gc, runwayStartX, labelYPos, lda*scaleFactor, Integer.toString(lda)+"m (LDA)");
        drawHorizontalBar(gc, runwayStartX+lda*scaleFactor, labelYPos, 60*scaleFactor, "\n\n"+Integer.toString(60)+"m (60)");
        drawHorizontalBar(gc, runwayStartX+(lda+60)*scaleFactor, labelYPos, resa*scaleFactor, Integer.toString(resa)+"m (RESA)");
        gc.fillText("Landing from left to right", 30,30);
        break;
      case "TOA":
        // Obstacle
        gc.setFill(Color.RED);
        oStartX = runwayEndX-(App.obstruction.getDistanceFromThresh()*scaleFactor);
        gc.fillRect(oStartX, runwayYTop-obstacleHeight, 5, obstacleHeight);
        drawVerticalBar(gc, oStartX+15, runwayYTop-obstacleHeight, obstacleHeight, " "+Integer.toString(App.obstruction.getHeight()) + "m");
        // Labels

        gc.fillText("Takeoff from right to left", 30,30);
        break;
      case "LO":
        // Obstacle
        gc.setFill(Color.RED);
        oStartX = runwayEndX-(App.obstruction.getDistanceFromThresh()*scaleFactor);
        gc.fillRect(oStartX, runwayYTop-obstacleHeight, 5, obstacleHeight);
        drawVerticalBar(gc, oStartX+15, runwayYTop-obstacleHeight, obstacleHeight, " "+Integer.toString(App.obstruction.getHeight()) + "m");
        // Labels
        drawHorizontalBar(gc, runwayStartX, labelYPos, lda*scaleFactor, Integer.toString(lda)+"m (LDA)");
        drawHorizontalBar(gc, runwayStartX+lda*scaleFactor, labelYPos, 60*scaleFactor, "\n\n"+Integer.toString(60)+"m (60)");
        drawHorizontalBar(gc, runwayStartX+(lda+60)*scaleFactor, labelYPos, resa*scaleFactor, Integer.toString(resa)+"m (RESA)");
        drawHorizontalBar(gc, oStartX-(h50*scaleFactor), labelYPos+43, h50*scaleFactor, Integer.toString(h50)+"m (hx50)");
        gc.strokeLine(oStartX-(h50*scaleFactor), runwayYTop, oStartX, runwayYTop-obstacleHeight);
        gc.strokeLine(oStartX-(h50*scaleFactor-60*scaleFactor), runwayYTop, oStartX-60*scaleFactor, runwayYTop-obstacleHeight);
        gc.fillText("Landing from right to left", 30,30);
        break;
    }
  }

  private void drawHorizontalBar(GraphicsContext gc, double x, double y, double l, int dist){
    drawHorizontalBar(gc, x,  y, l, Integer.toString(dist));
  }
  private void drawHorizontalBar(GraphicsContext gc, double x, double y, double l, String label){
    gc.setStroke(Color.WHITE);
    gc.setLineWidth(2);
    gc.strokeLine(x,y,x+l,y);
    gc.strokeLine(x,y-5,x,y+5);
    gc.strokeLine(x+l,y-5,x+l,y+5);
    gc.setFill(Color.WHITE);
    gc.fillText(label, x + (l/2) - (label.length()/2*5), y-7);
  }
  private void drawVerticalBar(GraphicsContext gc, double x, double y, double l, int dist){
    drawVerticalBar(gc, x, y, l, Integer.toString(dist));
  }
  private void drawVerticalBar(GraphicsContext gc, double x, double y, double l, String label){
    gc.setStroke(Color.WHITE);
    gc.setLineWidth(2);
    gc.strokeLine(x,y,x,y+l);
    gc.strokeLine(x-5,y,x+5,y);
    gc.strokeLine(x-5,y+l,x+5,y+l);
    gc.setFill(Color.WHITE);
    gc.fillText(label, x + 5, y+(l/2));
  }


  public void setTO(ActionEvent actionEvent) {
    isTakeoff = true;
    menu.setText(takeoff.getText());
    drawSideView();
  }

  public void setL(ActionEvent actionEvent) {
    isTakeoff = false;
    menu.setText(landing.getText());
    drawSideView();
  }
}
