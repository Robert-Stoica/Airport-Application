package org.comp2211;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comp2211.calculations.Calculations;
import org.comp2211.calculations.Obstruction;
import org.comp2211.media.Media;
import org.comp2211.media.XMLData;

/**
 * Screen to input data related to an obstacle, or obstruction.
 *
 * @author MGhee
 */
public class ObstacleInput {

  private static final Logger logger = LogManager.getLogger(ObstacleInput.class);
  private final FileChooser fileChooser = new FileChooser();
  /** Height of the obstacle, in metres. */
  @FXML private TextField height;
  /** Distance along the centre line, in metres. */
  @FXML private TextField centre;
  /** Distance from the threshold, in metres. */
  @FXML private TextField threshold;
  /** Blast protection. */
  @FXML private TextField sideText;

  @FXML private MenuButton menu;
  @FXML private MenuItem away;
  @FXML private MenuItem towards;
  /** Sidebar vbox. */
  @FXML private VBox vbox;

  @FXML private Button submit;
  @FXML private Button clear;
  @FXML private Button contrastB;
  /** True if the CSS should be in high contrast mode. */
  private Boolean highContrast = false;

  @FXML private ToggleButton toggle;
  @FXML private Button importB;
  @FXML private Button exportB;
  private Obstruction obstacle;
  private Calculations calculator;
  /** True if the sidebar is visible. */
  private Boolean sideBar = false;

  @FXML private HBox manual;

  @FXML private HBox invalid;


  /**
   * Switches to the visualisation screen. This function also checks that the user gave valid input,
   * and recalculates the runway.
   */
  @FXML
  public void openVisual() {
    logger.info("We have opened the visualization of the runway");
    try {
      if (!(menu.getText().equals("Operation Type")
          || height.getText().isBlank()
          || centre.getText().isBlank()
          || threshold.getText().isBlank())) {
        obstacle =
            new Obstruction(
                Integer.parseInt(centre.getText()),
                Integer.parseInt(height.getText()),
                Integer.parseInt(threshold.getText()));
        logger.info("Centre: {}", centre.getText());
        logger.info("Height: {}", height.getText());
        logger.info("Threshold: {}", threshold.getText());
        calculator = new Calculations();
        try {
          App.runway.setbProtection(Integer.parseInt(sideText.getText()));
          logger.info("Blast Protection set to {}", sideText.getText());
        } catch (NumberFormatException ignored) {
        }

        App.obstruction = obstacle;

        if (menu.getText().equals(away.getText())) {
          logger.info("Doing calculations for the Away and Over landing");
          logger.info("TORA: {}", App.runway.getTora());
          logger.info("Blast protection: {}", App.runway.getbProtection());
          logger.info("Displaced threshold: {}", App.runway.getDisplacedThreshold());
          logger.info("Distance from threshold: {}", App.obstruction.getDistanceFromThresh());
          calculator.recalculateToraAwayOver(App.runway, obstacle);
          calculator.recalculateTodaAwayOver(App.runway);
          calculator.recalculateAsdaAwayOver(App.runway);
          calculator.recalculateLdaAwayOver(App.runway, obstacle);
          logger.info(
              "New TORA: {}", calculator.recalculateToraAwayOver(App.runway, obstacle).getTora());
          RunwayVisual.isAwayOver = true;
        } else if (menu.getText().equals(towards.getText())) {
          logger.info("Doing calculations for the Towards landing");
          calculator.recalculateToraTowards(App.runway, obstacle);
          calculator.recalculateTodaTowards(App.runway);
          calculator.recalculateAsdaTowards(App.runway);
          calculator.recalculateLdaTowards(App.runway, obstacle);
          logger.info(
              "New TORA: {}", calculator.recalculateToraTowards(App.runway, obstacle).getTora());
          RunwayVisual.isAwayOver = false;
        }
        if (App.runway.getTora() < 0 || App.runway.getToda() < 0 || App.runway.getAsda() < 0 || App.runway.getLda() < 0) {
          showInvalid();
        } else{
          App.setRoot("visual");
        }
      } else {
        logger.error("One of the fields is empty");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Makes the sidebar elements visible or invisible on the screen. This also recalculates the
   * runway.
   *
   * @author JoshPattman
   */
  @FXML
  public void openSidebar() {

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
    if (sideBar) {
      sideBar = false;
      deleteSide();
    } else {
      logger.info("Show the sidebar");
      sideBar = true;
      vbox.setVisible(true);
    }
  }

  /**
   * Shows the manual.
   *
   * @author mccaw12
   */
  @FXML
  public void showManual() {
    manual.setVisible(true);
  }

  /**
   * Hides the manual.
   *
   * @author mccaw12
   */
  @FXML
  public void hideManual() {
    manual.setVisible(false);
  }

  @FXML
  public void hideInvalid() {
    invalid.setVisible(false);
  }

  public void showInvalid() {
    invalid.setVisible(true);
  }

  /** Changes the menu text. */
  @FXML
  public void changeT() {
    logger.info("Change the menu text");
    menu.setText(away.getText());
  }

  /** Also changes the menu text. */
  @FXML
  public void changeTe() {
    logger.info("Change the menu text");
    menu.setText(towards.getText());
  }

  /** Clears all the text inputs. */
  @FXML
  private void clearText() {
    logger.info("The input has been cleared");
    height.clear();
    centre.clear();
    threshold.clear();
  }

  /** Hides the sidebar. */
  @FXML
  public void deleteSide() {
    logger.info("Hide the sidebar");
    vbox.setVisible(false);
    sideText.clear();
  }

  /**
   * Exports the current obstacle to XML. This function also checks that the user gave valid input.
   */
  @FXML
  public void export() {
    if (!(height.getText().isBlank()
        || centre.getText().isBlank()
        || threshold.getText().isBlank())) {
      logger.info("Creating the new obstacle and creating a xml that contains it");
      Stage newWindow = new Stage();
      newWindow.setTitle("Save Obstacle");
      obstacle =
          new Obstruction(
              Integer.parseInt(centre.getText()),
              Integer.parseInt(height.getText()),
              Integer.parseInt(threshold.getText()));
      File defaultPath = new File(System.getProperty("user.home")+"/obstacles");
      defaultPath.mkdirs();
      fileChooser.setInitialDirectory(defaultPath);
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
    } else {
      logger.error("One or more fields are empty, cannot export xml file");
    }
  }

  /** Imports an obstacle from an XML file. */
  @FXML
  public void create() {
    logger.info("Input the xml and autofill the text fields");
    Stage newWindow = new Stage();
    newWindow.setTitle("Open Obstacle");
    File defaultPath = new File(System.getProperty("user.home")+"/obstacles");
    defaultPath.mkdirs();
    fileChooser.setInitialDirectory(defaultPath);
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

  /**
   * Toggles high contrast mode.
   *
   * @author snow6701
   */
  public void changeContrast() {
    if (highContrast) {
      highContrast = false;
      clear.getStyleClass().clear();
      submit.getStyleClass().clear();
      contrastB.getStyleClass().clear();
      toggle.getStyleClass().clear();
      importB.getStyleClass().clear();
      exportB.getStyleClass().clear();
      clear.getStyleClass().add("button");
      submit.getStyleClass().add("button");
      contrastB.getStyleClass().add("button");
      toggle.getStyleClass().add("button");
      importB.getStyleClass().add("button");
      exportB.getStyleClass().add("button");
    } else {
      highContrast = true;
      clear.getStyleClass().add("button2");
      submit.getStyleClass().add("button2");
      contrastB.getStyleClass().add("button2");
      toggle.getStyleClass().add("button2");
      importB.getStyleClass().add("button2");
      exportB.getStyleClass().add("button2");
    }
  }
  public void keyListener(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.ENTER){
            openVisual();
        }
  }
}
