package org.comp2211;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import org.comp2211.calculations.Runway;
import org.comp2211.media.Media;
import org.comp2211.media.XMLData;

public class RunwayInput {

  private final FileChooser fileChooser = new FileChooser();
  private Runway runway;
  @FXML private Button clear;
  @FXML private Button submit;
  @FXML private TextField originalTora;
  @FXML private TextField originalLda;
  @FXML private TextField displacedThreshold;
  @FXML private MenuButton menu;

  // Calls a method where we import XML file
  @FXML private MenuItem addRunway;

  // Calls a method where we export XML file
  @FXML private Button exportXml;
  
  @FXML private Button contrastB;
  private Boolean highContrast = false;
  

  @FXML
  private void clearText() {
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
      originalTora.setText(String.valueOf(runway.getOriginalTora()));
      originalLda.setText(String.valueOf(runway.getOriginalLda()));
      displacedThreshold.setText(String.valueOf(runway.getDisplacedThreshold()));
    }
  }

  @FXML
  private void export() {
	  if (!(originalTora.getText().isBlank()|| originalLda.getText().isBlank()|| displacedThreshold.getText().isBlank())) {
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
  
  public void changeContrast() {
	  if(highContrast) {
		  highContrast = false;
		  clear.getStyleClass().clear();
		  submit.getStyleClass().clear();
		  exportXml.getStyleClass().clear();
		  contrastB.getStyleClass().clear();
		  clear.getStyleClass().add("button");
		  submit.getStyleClass().add("button");
		  exportXml.getStyleClass().add("button");
		  contrastB.getStyleClass().add("button");
	  }else {
		  highContrast = true;
		  clear.getStyleClass().add("button2");
		  submit.getStyleClass().add("button2");
		  exportXml.getStyleClass().add("button2");
		  contrastB.getStyleClass().add("button2");
	  }
	  
  }
  
}
