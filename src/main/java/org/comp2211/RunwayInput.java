package org.comp2211;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comp2211.calculations.Calculations;
import org.comp2211.calculations.Runway;
import org.comp2211.media.Media;
import org.comp2211.media.XMLData;

public class RunwayInput {

    private final FileChooser fileChooser = new FileChooser();
    private Runway runway;
    @FXML private TextField originalTora;
    @FXML private TextField originalLda;
    @FXML private TextField displacedThreshold;
    @FXML private MenuButton menu;
    Scene scene = null;
    Parent root = null;
    private Stage stage ;
    private static final Logger logger = LogManager.getLogger(RunwayInput.class);
    @FXML private Button submit;
    @FXML private Button clear;
    @FXML private ToggleButton toggle;
    @FXML private Button importB;
    @FXML private Button exportB;
    @FXML private Button helpButton;
// Calls a method where we export XML file
  @FXML private Button exportXml;
  @FXML private Button contrastB;
  private Boolean highContrast = false;


    @FXML
    private void clearText() {
        logger.info("Clear the input");
        originalTora.clear();
        originalLda.clear();
        displacedThreshold.clear();
    }

    private boolean createRunway() {
        try {
            if (!(menu.getText().equals("S elect Runway")
                    || originalTora.getText().isBlank()
                    || originalLda.getText().isBlank()
                    || displacedThreshold.getText().isBlank())) {
                logger.info("Creating the new Runway with the values inside the text field");
                runway =
                        new Runway(
                                menu.getText(),
                                Integer.parseInt(originalTora.getText()),
                                Integer.parseInt(originalLda.getText()),
                                Integer.parseInt(displacedThreshold.getText()));
                return true;
            } else {
                System.out.println("One of the fields is empty");
            }
        } catch (Exception e) {
            System.out.println("Invalid Input");
        }
        return false;
    }

    @FXML
    private void openObstacle() throws IOException {
        if (createRunway()) {
            App.runway = runway;
            App.setRoot("Obstacle");
        }
    }

    @FXML
    private void addRunway() {
        logger.info("Import the xml");
        Stage newWindow = new Stage();
        newWindow.setTitle("Open Runway");
        File file = fileChooser.showOpenDialog(newWindow);
        if (file != null) {
            XMLData data;
            try {
                data = Media.importXML(file);
            } catch (XMLStreamException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("XML error");
                alert.setHeaderText("Error on opening file");
                alert.setContentText("File could not be opened");
                alert.showAndWait();
                return;
            }
            if (data.runways.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("File empty");
                alert.setHeaderText("Error on opening file");
                alert.setContentText("File contained no runways.");
                alert.showAndWait();
                return;
            }
            runway = data.runways.get(0);
            logger.info("Imput the actual values inside the text fields");
            originalTora.setText(String.valueOf(runway.getOriginalTora()));
            originalLda.setText(String.valueOf(runway.getOriginalLda()));
            displacedThreshold.setText(String.valueOf(runway.getDisplacedThreshold()));
        }
    }

    @FXML
    private void export() {
        if (!(originalTora.getText().isBlank()|| originalLda.getText().isBlank()|| displacedThreshold.getText().isBlank())) {
            logger.info("Export the runway to an xml file");
            Stage newWindow = new Stage();
            newWindow.setTitle("Save Runway");
            File file = fileChooser.showSaveDialog(newWindow);
            if (file != null) {
                var data = new XMLData();
                if (runway != null) {
                    data.runways.add(runway);
                }
                try {
                    Media.exportXML(data, file);
                } catch (XMLStreamException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("XML error");
                    alert.setHeaderText("Error on writing file");
                    alert.setContentText("File could not be written");
                    alert.showAndWait();
                }
            }
        }else {
            System.out.println("One or more fields are empty, cannot export xml file");
        }
    }


    @FXML
    private void openHelp() throws IOException {
        logger.info("Opened the Help Page");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Help" + ".fxml"));
        root = fxmlLoader.load();
        scene = new Scene(root);
        stage = new Stage();

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        App.stg = stage;


    }

  
  public void changeContrast() {
	  if(highContrast) {
		  highContrast = false;
		  clear.getStyleClass().clear();
		  submit.getStyleClass().clear();
		  exportXml.getStyleClass().clear();
		  contrastB.getStyleClass().clear();
		  helpButton.getStyleClass().clear();
		  clear.getStyleClass().add("button");
		  submit.getStyleClass().add("button");
		  exportXml.getStyleClass().add("button");
		  contrastB.getStyleClass().add("button");
		  helpButton.getStyleClass().add("button");
	  }else {
		  highContrast = true;
		  clear.getStyleClass().add("button2");
		  submit.getStyleClass().add("button2");
		  exportXml.getStyleClass().add("button2");
		  contrastB.getStyleClass().add("button2");
		  helpButton.getStyleClass().add("button2");
	  }
	  
  }
}
