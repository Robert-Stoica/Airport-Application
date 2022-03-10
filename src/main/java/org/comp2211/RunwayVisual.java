package org.comp2211;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.comp2211.Calculations.Calculations;
import org.comp2211.Calculations.Runway;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RunwayVisual {

    public static boolean isAwayOver;

    @FXML
    private Button calculation;
    @FXML
    private Button goback;
    @FXML
    private Label tora;
    @FXML
    private Label toda;
    @FXML
    private Label asda;
    @FXML
    private Label lda;

    private String format = "TORA = Original TORA - Blast Protection - Obstacle distance from threshold - Displaced Thresold\n" +
            "\t = %d - %d - %d - %d\n" +
            "\t = %d\n" +
            "ASDA = (R)TORA + STOPWAY\n" +
            "\t = %d\n" +
            "TODA = (R)TORA + CLEARWAY\n" +
            "\t = %d\n" +
            "LDA  = (O)LDA - Obstacle distance from threshold - Strip end - Slope calculation\n" +
            "\t = %d - %d - %d - %d\n" +
            "\t = %d";

    void safeWriteFile(String filename, String data){
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
        var copyRunway = new Runway("copy", App.runway.getOriginalTora(), App.runway.getOriginalLda(), App.runway.getDisplacedThreshold());
        var oTora = copyRunway.getTora();
        var oLda = copyRunway.getLda();
        var dThresh = copyRunway.getDisplacedThreshold();

        int tora;
        int asda;
        int toda;
        int lda;

        if (isAwayOver){
            calc.recalculateToraAwayOver(copyRunway, App.obstruction);
            tora = copyRunway.getTora();
            calc.recalculateAsdaAwayOver(copyRunway);
            asda = copyRunway.getAsda();
            calc.recalculateTodaAwayOver(copyRunway);
            toda = copyRunway.getToda();
            calc.recalculateLdaAwayOver(copyRunway, App.obstruction);
            lda = copyRunway.getLda();
        } else{
            calc.recalculateToraTowards(copyRunway, App.obstruction);
            tora = copyRunway.getTora();
            calc.recalculateAsdaTowards(copyRunway);
            asda = copyRunway.getAsda();
            calc.recalculateTodaTowards(copyRunway);
            toda = copyRunway.getToda();
            calc.recalculateLdaTowards(copyRunway, App.obstruction);
            lda = copyRunway.getLda();
        }

        var calculationsString = String.format(format, oTora, copyRunway.getbProtection(), App.obstruction.getDistanceFromThresh(), dThresh, tora, asda, toda, oLda, App.obstruction.getDistanceFromThresh(), copyRunway.getStripEnd(), 0, lda);

        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                safeWriteFile("filename.txt", calculationsString);
            } else {
                System.out.println("File already exists.");
                safeWriteFile("filename.txt", calculationsString);
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
}
