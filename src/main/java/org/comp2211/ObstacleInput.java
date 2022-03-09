package org.comp2211;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ObstacleInput {


    @FXML
    private Button submit;
    @FXML
    private TextField height;
    @FXML
    private TextField length;
    @FXML
    private TextField center;
    @FXML
    private TextField treshold;

    @FXML
    public void openVisual() throws IOException {
        App.setRoot("visual");
    }

    @FXML
    public void openSidebar() {
        // toggle open sidebar
    }
}
