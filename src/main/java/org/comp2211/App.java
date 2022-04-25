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

  static void setRoot(String fxml) throws IOException {
    logger.info("We have loaded a new Scene");
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    logger.info("The app has launched");
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    this.stage = stage;
    stage.setTitle("Airport Recalculation");
    stage.setOnCloseRequest(ev -> shutdown());

    runwayInput();
  }

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
