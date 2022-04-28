import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.comp2211.App;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
public class AppTest {

  App app = new App();

  @Start
  void start(Stage stage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("Input.fxml"));
    stage.setTitle("InputScene Test");
    stage.setScene(new Scene(root));
    stage.show();
    stage.toFront();
    app.start(stage);
    app.runwayInput();
  }

  @Test
  void checkHasAllComponets() {
    FxAssert.verifyThat("#menu", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#originalTora", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#originalLda", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#displacedThreshold", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#clear", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#exportXml", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#submit", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#contrastB", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#helpButton", NodeMatchers.isNotNull());

  }

  @Test
  void checkTextInputs(FxRobot robot){
      robot.clickOn("#originalTora").write("921");
      robot.clickOn("#originalLda").write("120");
      robot.clickOn("#displacedThreshold").write("762");
      FxAssert.verifyThat("#originalTora", NodeMatchers.hasChild("921"));
      FxAssert.verifyThat("#originalLda", NodeMatchers.hasChild("120"));
      FxAssert.verifyThat("#displacedThreshold", NodeMatchers.hasChild("762"));
  }

  @Test
  void checkEmailPanel(FxRobot robot) throws Exception {
    robot.clickOn("#helpButton");
    FxAssert.verifyThat("#receiver", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#subject", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#sender", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#area", NodeMatchers.isNotNull());

}

}
