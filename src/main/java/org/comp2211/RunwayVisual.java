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

/**
 * Displays a visual representation of the recalculated runway to the screen, using magic.
 *
 * @author JoshPattman
 */
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
    private Canvas canvas;
    @FXML
    private MenuButton menu;
    @FXML
    private MenuItem landing;
    @FXML
    private MenuItem takeoff;

    Color DarkGreen = Color.color(51 / 255.0, 204 / 255.0, 51 / 255.0);
    Color Purple = Color.color(153 / 255.0, 0 / 255.0, 255 / 255.0);
    Color DarkBlue = Color.color(51 / 255.0, 51 / 255.0, 204 / 255.0);
    Color SkyBlue = Color.color(85 / 255.0, 216 / 255.0, 255 / 255.0);
    Color AsphaltGrey = Color.color(150 / 255.0, 150 / 255.0, 150 / 255.0);

    boolean isTakeoff;

    private static final Logger logger = LogManager.getLogger(Calculations.class);
    private boolean highContrast = false;


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
            } else {
                System.out.println("File already exists.");
            }
            safeWriteFile("calculations.txt", calculationsString);
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
        if (menu.getText().equals("Landing/Takeoff")) {
            drawBlankCanvas();
        }
    }

    public void newRunway() throws IOException {
        App.setRoot("Input");
    }

    private void drawBlankCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(SkyBlue);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillText("Select either landing or takeoff to continue", canvas.getWidth() / 2 - 150, canvas.getHeight() / 2);
    }

    private void drawSideView() {
        // Drawing stuff
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        double runwayYTop = height / 2.5;
        double runwayDepth = 10;

        double runwayPadding = 30;

        double runwayStartX = runwayPadding;
        double runwayEndX = width - runwayPadding;
        double originalRunwayLength = App.runway.getOriginalTora() + 60;
        double scaleFactor = (runwayEndX - runwayStartX) / originalRunwayLength;

        double verticalExtraScaleFactor = 10;
        double obstaclePixelHeight = App.obstruction.getHeight() * scaleFactor * verticalExtraScaleFactor;
        // Grass
        gc.setFill(DarkGreen);
        gc.fillRect(0, runwayYTop, width, height - runwayYTop);

        // Sky
        gc.setFill(SkyBlue);
        gc.fillRect(0, 0, width, runwayYTop);

        // Runway
        gc.setFill(AsphaltGrey);
        gc.fillRect(runwayPadding, runwayYTop, width - (runwayPadding * 2), runwayDepth);

        String mode = "";
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

        if (mode.equals("TOT")) {
            // Positions
            var threshold = 0;
            var displacedThreshold = threshold + App.runway.getDisplacedThreshold();
            var toraEnd = displacedThreshold + App.runway.getTora();
            var todaEnd = displacedThreshold + App.runway.getToda();
            var asdaEnd = displacedThreshold + App.runway.getAsda();
            var stripEndStart = toraEnd;
            var stripEndEnd = stripEndStart + App.runway.getStripEnd();
            var resaEnd = stripEndEnd + App.runway.getResa();
            var heightCalcEnd = stripEndEnd + App.obstruction.getHeight() * 50;
            var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;
            var pcc = new PixelCoordinateConverter(scaleFactor, runwayStartX);

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
            gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5, obstaclePixelHeight);
            drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight, obstaclePixelHeight, " " + App.obstruction.getHeight()
                + "m");

            // Labels
            if (toraEnd == todaEnd && todaEnd == asdaEnd) {
                drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos, pcc.conv(toraEnd), toraString + "m (TORA,TODA,ASDA)");
            } else {
                drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos, pcc.conv(toraEnd), toraString + "m (TORA)");
                drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos + 20, pcc.conv(todaEnd), todaString + "m (TODA)");
                drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos + 40, pcc.conv(asdaEnd), asdaString + "m (ASDA)");
            }
            drawHorizontalBarBetween(gc, pcc.conv(stripEndStart), labelYPos, pcc.conv(stripEndEnd), "\n\n" + seString + "m (SE)");
            drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos, pcc.conv(resaEnd), resaString + "m (RESA)");
            drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos + 43, pcc.conv(heightCalcEnd), heightCalcString + "m (hx50)");
            gc.fillText("Takeoff from left to right", 30, 30);
        } else if (mode.equals("LT")) {
            // Positions
            var threshold = 0;
            var displacedThreshold = threshold + App.runway.getDisplacedThreshold();
            var ldaEnd = displacedThreshold + App.runway.getLda();
            var stripEndEnd = ldaEnd + App.runway.getStripEnd();
            var resaEnd = stripEndEnd + App.runway.getResa();
            var obstacle = App.obstruction.getDistanceFromThresh() + displacedThreshold;
            var pcc = new PixelCoordinateConverter(scaleFactor, runwayStartX);

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
            gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5, obstaclePixelHeight);
            drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight, obstaclePixelHeight, " " + App.obstruction.getHeight()
                + "m");

            // Labels
            drawHorizontalBarBetween(gc, pcc.conv(displacedThreshold), labelYPos, pcc.conv(ldaEnd), ldaString + "m (LDA)");
            drawHorizontalBarBetween(gc, pcc.conv(ldaEnd), labelYPos, pcc.conv(stripEndEnd), "\n\n" + seString + "m (SE)");
            drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos, pcc.conv(resaEnd), resaString + "m (RESA)");
            gc.fillText("Landing from left to right", 30, 30);
        } else if (mode.equals("TOA")) {
            // Positions
            var threshold = 0;
            var displacedThreshold = threshold + App.runway.getDisplacedThreshold();
            var ebaEnd = displacedThreshold + 300; // TODO: FIGURE OUT HOW TO GET EBA
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
            gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5, obstaclePixelHeight);
            drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight, obstaclePixelHeight, " " + App.obstruction.getHeight()
                + "m");

            // Labels
            drawHorizontalBarBetween(gc, pcc.conv(ebaEnd), labelYPos, pcc.conv(displacedThreshold), ebaString + "m (EBA)");
            if (toraEnd == todaEnd && todaEnd == asdaEnd) {
                drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(ebaEnd), toraString + "m (TORA,TODA,ASDA)");
            } else {
                drawHorizontalBarBetween(gc, pcc.conv(toraEnd), labelYPos, pcc.conv(ebaEnd), toraString + "m (TORA)");
                drawHorizontalBarBetween(gc, pcc.conv(todaEnd), labelYPos + 20, pcc.conv(ebaEnd), todaString + "m (TODA)");
                drawHorizontalBarBetween(gc, pcc.conv(asdaEnd), labelYPos + 40, pcc.conv(ebaEnd), asdaString + "m (ASDA)");
            }
            gc.fillText("Takeoff from right to left", 30, 30);
        } else {
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
            gc.fillRect(pcc.conv(obstacle) - 2.5, runwayYTop - obstaclePixelHeight, 5, obstaclePixelHeight);
            drawVerticalBar(gc, pcc.conv(obstacle) + 15, runwayYTop - obstaclePixelHeight, obstaclePixelHeight, " " + App.obstruction.getHeight()
                + "m");

            // Labels
            drawHorizontalBarBetween(gc, pcc.conv(heightCalcResaEnd), labelYPos + 45, pcc.conv(heightCalcStart), heightCalcString + "m (hx50)");
            drawHorizontalBarBetween(gc, pcc.conv(heightCalcResaEnd), labelYPos, pcc.conv(resaStart), resaString + "m (RESA)");
            drawHorizontalBarBetween(gc, pcc.conv(stripEndEnd), labelYPos, pcc.conv(heightCalcResaEnd), "\n\n" + seString + "m (SE)");
            drawHorizontalBarBetween(gc, pcc.conv(ldaEnd), labelYPos, pcc.conv(stripEndEnd), ldaString + "m (LDA)");
            gc.fillText("Landing from right to left", 30, 30);
        }

    }

    private void drawHorizontalBarBetween(GraphicsContext gc, double x, double y, double x2, String label) {
        drawHorizontalBar(gc, x, y, x2 - x, label);
    }

    private void drawHorizontalBar(GraphicsContext gc, double x, double y, double l, String label) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(x, y, x + l, y);
        gc.strokeLine(x, y - 5, x, y + 5);
        gc.strokeLine(x + l, y - 5, x + l, y + 5);
        gc.setFill(Color.WHITE);
        gc.fillText(label, x + (l / 2) - (label.length() / 2 * 5), y - 7);
    }

    private void drawVerticalBarBetween(GraphicsContext gc, double x, double y, double y2, String label) {
        drawVerticalBar(gc, x, y, y2 - y, label);
    }

    private void drawVerticalBar(GraphicsContext gc, double x, double y, double l, String label) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(x, y, x, y + l);
        gc.strokeLine(x - 5, y, x + 5, y);
        gc.strokeLine(x - 5, y + l, x + 5, y + l);
        gc.setFill(Color.WHITE);
        gc.fillText(label, x + 5, y + (l / 2));
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

    private class PixelCoordinateConverter {
        double scaleFactor;
        double offset;

        public PixelCoordinateConverter(double scaleFactor, double offset) {
            this.scaleFactor = scaleFactor;
            this.offset = offset;
        }

        public double conv(double x) {
            return x * scaleFactor + offset;
        }
    }

    public void changeContrast() {
        if (highContrast) {
            highContrast = false;
            calculation.getStyleClass().clear();
            goback.getStyleClass().clear();
            contrastB.getStyleClass().clear();
            calculation.getStyleClass().add("button");
            goback.getStyleClass().add("button");
            contrastB.getStyleClass().add("button");
        } else {
            highContrast = true;
            calculation.getStyleClass().add("button2");
            goback.getStyleClass().add("button2");
            contrastB.getStyleClass().add("button2");
        }

    }
}