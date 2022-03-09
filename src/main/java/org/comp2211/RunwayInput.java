package org.comp2211;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import org.comp2211.Calculations.Runway;

import java.io.IOException;


public class RunwayInput {

    private Runway runway;

    @FXML
    private Button clear;
    @FXML
    private Button submit;
    @FXML
    private TextField originalTora;
    @FXML
    private TextField originalLda;
    @FXML
    private TextField displacedThreshold;
    @FXML
    private MenuButton menu;
    
    //Calls a method where we import XML file
    @FXML
    private MenuItem addRunway;
    
    //Calls a method where we export XML file
    @FXML
    private Button exportXml;

    @FXML
    private void clearText() throws IOException {
    	originalTora.clear();
    	originalLda.clear();
        displacedThreshold.clear();
    }

    @FXML
    private void openObstacle() throws IOException {
    	try {
    		//Need to code the Select Runway drop down so it actually selects a menu item
    	if(!(menu.getText().equals("Select Runway") || originalTora.getText().isBlank() || originalLda.getText().isBlank() || displacedThreshold.getText().isBlank())) {
    		runway = new Runway(menu.getText(), Integer.parseInt(originalTora.getText()), Integer.parseInt(originalLda.getText()), Integer.parseInt(displacedThreshold.getText()));
    		/*
    		System.out.println(Integer.parseInt(originalTora.getText()));
    		System.out.println(Integer.parseInt(originalLda.getText()));
    		System.out.println(Integer.parseInt(displacedThreshold.getText()));
    		*/
    		App.setRoot("Obstacle");
    	}else {
    		System.out.println("One of the fields is empty");
    	}
    	}catch(Exception e) {
    		System.out.println("Invalid Input");
    	}
    }

}




