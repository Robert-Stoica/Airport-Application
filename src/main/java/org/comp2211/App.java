package org.comp2211;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.comp2211.Calculations.Obstruction;
import org.comp2211.Calculations.Runway;

import java.io.IOException;

/** JavaFX App */
public class App extends Application {


    public static Runway runway;
    public static Obstruction obstruction;
  private static Scene scene;
  private Stage stage;

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
      this.stage = stage;
      stage.setTitle("Airport Recalculation");
      stage.setOnCloseRequest(ev -> {
          shutdown();
      });

      runwayInput();
  }

    private void shutdown() {
        System.exit(0);
    }

    public void runwayInput() throws IOException {

        scene = new Scene(loadFXML("Input"));

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public void openRunwayvisualisation() throws IOException {

        scene = new Scene(loadFXML("primary"));

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

}
