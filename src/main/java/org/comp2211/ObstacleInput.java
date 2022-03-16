package org.comp2211;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import org.comp2211.calculations.Calculations;
import org.comp2211.calculations.Obstruction;
import org.comp2211.media.Media;
import org.comp2211.media.XMLData;

public class ObstacleInput {

  private final FileChooser fileChooser = new FileChooser();
  @FXML private Button submit;
  @FXML private Button clear;
  @FXML private TextField height;
  @FXML private TextField centre;
  @FXML private TextField threshold;
  @FXML private TextField sideText;
  @FXML private MenuButton menu;
  @FXML private MenuItem away;
  @FXML private MenuItem towards;
  @FXML private VBox vbox;
  private Obstruction obstacle;
  private Calculations calculator;

  @FXML
  public void openVisual() {
    try {
      if (!(menu.getText().equals("Operation Type")
              || height.getText().isBlank()
              || centre.getText().isBlank())
          || threshold.getText().isBlank()) {
        obstacle =
            new Obstruction(
                Integer.parseInt(centre.getText()),
                Integer.parseInt(height.getText()),
                Integer.parseInt(threshold.getText()));
        System.out.println(Integer.parseInt(centre.getText()));
        System.out.println(Integer.parseInt(height.getText()));
        System.out.println(Integer.parseInt(threshold.getText()));
        calculator = new Calculations();
        if (!sideText.getText().isBlank()) {
          App.runway.setbProtection(Integer.parseInt(sideText.getText()));
          System.out.println(App.runway.getbProtection());
        }

        App.obstruction = obstacle;

        if (menu.getText().equals(away.getText())) {
          calculator.recalculateToraAwayOver(App.runway, obstacle);
          calculator.recalculateTodaAwayOver(App.runway);
          calculator.recalculateAsdaAwayOver(App.runway);
          calculator.recalculateLdaAwayOver(App.runway, obstacle);
          RunwayVisual.isAwayOver = true;
        } else if (menu.getText().equals(towards.getText())) {
          calculator.recalculateToraTowards(App.runway, obstacle);
          calculator.recalculateTodaTowards(App.runway);
          calculator.recalculateAsdaTowards(App.runway);
          calculator.recalculateLdaTowards(App.runway, obstacle);
          RunwayVisual.isAwayOver = false;
        }
        App.setRoot("visual");
      } else {
        System.out.println("One of the fields is empty");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void openSidebar() {
    vbox.setVisible(true);
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
  private void clearText() {
    height.clear();
    centre.clear();
  }

  @FXML
  public void deleteSide(MouseEvent ignoredEvent) {
    vbox.setVisible(false);
  }

  @FXML
  public void export() {
    Stage newWindow = new Stage();
    newWindow.setTitle("Save Obstacle");
    obstacle =
        new Obstruction(
            Integer.parseInt(centre.getText()),
            Integer.parseInt(height.getText()),
            Integer.parseInt(threshold.getText()));
    File file = fileChooser.showSaveDialog(newWindow);
    if (file != null) {
      var data = new XMLData();
      if (obstacle != null) {
        data.obstructions.add(obstacle);
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
  }

  @FXML
  public void create() {
    Stage newWindow = new Stage();
    newWindow.setTitle("Open Obstacle");
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
      if (data.obstructions.size() == 0) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("File empty");
        alert.setHeaderText("Error on opening file");
        alert.setContentText("File contained no obstructions.");
        alert.showAndWait();
        return;
      }
      obstacle = data.obstructions.get(0);
      height.setText(String.valueOf(obstacle.getHeight()));
      centre.setText(String.valueOf(obstacle.getDistanceFromCl()));
      threshold.setText(String.valueOf(obstacle.getDistanceFromThresh()));
    }
  }
}
