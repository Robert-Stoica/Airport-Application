package org.comp2211;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.comp2211.Calculations.Runway;

import java.io.IOException;


public class RunwayInput {



    private Runway runway;

    @FXML
    private Button clear;
    @FXML
    private Button submit;
    @FXML
    private TextField tora;
    @FXML
    private TextField stopway;
    @FXML
    private TextField clearway;
    @FXML
    private TextField threshold;

    @FXML
    private void clearText() throws IOException {
        tora.clear();
        stopway.clear();
        clearway.clear();
        threshold.clear();
    }

    @FXML
    private void openObstacle() throws IOException {
        App.setRoot("Obstacle");
    }

}




