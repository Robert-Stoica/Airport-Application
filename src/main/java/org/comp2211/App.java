package org.comp2211;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.comp2211.calculations.Obstruction;
import org.comp2211.calculations.Runway;

/** JavaFX App. */
public class App extends Application {

  private static final Logger logger = LogManager.getLogger(App.class);
  public static Runway runway;
  public static Stage stg;
  public static Obstruction obstruction;
  private static Scene scene;
  private Stage stage;

  /**
   * Sets the root of a scene to a new fxml file, loaded from the defined repo.
   *
   * @param fxml The file to load
   * @throws IOException if the file could not be loaded.
   */
  static void setRoot(String fxml) throws IOException {
    logger.info("We have loaded a new Scene");
    scene.setRoot(loadFXML(fxml));
  }

  /**
   * Loads fxml from a defined repo.
   *
   * @param fxml The file to load
   * @return An FXML Parent node
   * @throws IOException if the file could not be loaded.
   */
  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /**
   * Launches the app.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    logger.info("The app has launched");
    launch();
  }

  /**
   * Starts the main app.
   *
   * @param stage Stage to show on
   * @throws IOException if the stage cannot be opened
   */
  @Override
  public void start(Stage stage) throws IOException {
    this.stage = stage;
    stage.setTitle("Airport Recalculation");
    stage.setOnCloseRequest(ev -> shutdown());

    runwayInput();
  }

  /** Closes the app. */
  private void shutdown() {
    logger.info("The app has closed");
    System.exit(0);
  }

  /**
   * Switches to the runway input screen.
   *
   * @throws IOException If the FXML fails to load.
   */
  public void runwayInput() throws IOException {

    scene = new Scene(loadFXML("Input"));

    stage.setScene(scene);
    stage.show();
    stage.centerOnScreen();
  }
}
