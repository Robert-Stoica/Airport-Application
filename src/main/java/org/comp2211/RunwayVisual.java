package org.comp2211;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RunwayVisual {

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

    public void createFile() {
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                try {
                    FileWriter myWriter = new FileWriter("filename.txt");
                    myWriter.write("Files in Java might be tricky, but it is fun enough!");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("File already exists.");
                try {
                    FileWriter myWriter = new FileWriter("filename.txt");
                    myWriter.write("asdasdsdasdasda");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
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
