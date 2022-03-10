package org.comp2211;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.io.IOException;

import org.comp2211.Calculations.Calculations;
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
    @FXML
    private TextField threshold;
    @FXML
    private MenuButton menu;
    @FXML
    private MenuItem away;
    @FXML
    private MenuItem towards;
    private Obstruction obstacle;
    private Calculations calculator;


    @FXML
    public void openVisual() throws IOException {
        try {
            if(!(menu.getText().equals("Operation Type") || height.getText().isBlank() || centre.getText().isBlank()) || threshold.getText().isBlank()) {
                obstacle = new Obstruction(Integer.parseInt(centre.getText()), Integer.parseInt(height.getText()),Integer.parseInt(threshold.getText()));
                System.out.println(Integer.parseInt(centre.getText()));
                System.out.println(Integer.parseInt(height.getText()));
                System.out.println(Integer.parseInt(threshold.getText()));
                calculator = new Calculations();
                App.obstruction = obstacle;
                if(menu.getText().equals(away.getText())){
                    System.out.println(App.runway.getTora());
                    System.out.println(App.runway.getbProtection());
                    System.out.println(App.runway.getDisplacedThreshold());
                    System.out.println(obstacle.getDistanceFromThresh());
                    calculator.recalculateToraAwayOver(App.runway,obstacle);
                    calculator.recalculateTodaAwayOver(App.runway);
                    calculator.recalculateAsdaAwayOver(App.runway);
                    calculator.recalculateLdaAwayOver(App.runway,obstacle);
                    System.out.println(calculator.recalculateToraAwayOver(App.runway,obstacle).getLda());
                    RunwayVisual.isAwayOver = true;
                }
                else if(menu.getText().equals(towards.getText())){
                    calculator.recalculateToraTowards(App.runway,obstacle);
                    calculator.recalculateTodaTowards(App.runway);
                    calculator.recalculateAsdaTowards(App.runway);
                    calculator.recalculateLdaTowards(App.runway,obstacle);
                    System.out.println(calculator.recalculateToraTowards(App.runway,obstacle).getTora());
                    RunwayVisual.isAwayOver = false;
                }
                App.setRoot("visual");
            }else {
                System.out.println("One of the fields is empty");
            }
        }catch(Exception e) {
            System.out.println(e);

        }
    }

    @FXML
    public void openSidebar() {
        // toggle open sidebar
    }

    @FXML
    public void changeT() {
        menu.setText(away.getText());
    }
    @FXML
    public void changeTe() {
        menu.setText(towards.getText());
    }
    
    @FXML
    private void clearText() throws IOException {
    	height.clear();
    	centre.clear();
    }
    
}
