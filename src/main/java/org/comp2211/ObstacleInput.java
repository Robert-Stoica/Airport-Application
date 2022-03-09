package org.comp2211;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

import org.comp2211.Calculations.Obstruction;
import org.comp2211.Calculations.Runway;

public class ObstacleInput {


    @FXML
    private Button submit;
    @FXML
    private Button clear;
    @FXML
    private TextField height;
    @FXML
    private TextField centre;
    
    private Obstruction obstacle;


    @FXML
    public void openVisual() throws IOException {
    	try {
    		//Need to code the Select Runway drop down so it actually selects a menu item
    	if(!(height.getText().isBlank() || centre.getText().isBlank())) {
    		obstacle = new Obstruction(Integer.parseInt(centre.getText()), Integer.parseInt(height.getText()));
    		System.out.println(Integer.parseInt(centre.getText()));
    		System.out.println(Integer.parseInt(height.getText()));
    		App.setRoot("visual");
    	}else {
    		System.out.println("One of the fields is empty");
    	}
    	}catch(Exception e) {
    		System.out.println("Invalid Input");
    	}
    }

    @FXML
    public void openSidebar() {
        // toggle open sidebar
    }
    
    @FXML
    private void clearText() throws IOException {
    	height.clear();
    	centre.clear();
    }
    
}
