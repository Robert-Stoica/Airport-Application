package org.comp2211;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        // create a file that has all the calculations in it
    }

    public void newRunway() throws IOException {
        App.setRoot("Input");
    }
}
