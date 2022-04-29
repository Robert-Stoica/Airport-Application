import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.comp2211.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.service.query.NodeQuery;

import java.awt.*;
import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
public class InputSceneTestGUI {

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
    FxAssert.verifyThat("#addpreset", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#originalTora", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#originalLda", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#displacedThreshold", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#clear", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#exportXml", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#submit", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#contrastB", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#helpButton", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#name", NodeMatchers.isNotNull());
  }

  void fillInputInputs(FxRobot robot, String name, String tora, String lda, String tresh) {
    robot.clickOn("#name").write(name);
    robot.clickOn("#originalTora").write(tora);
    robot.clickOn("#originalLda").write(lda);
    robot.clickOn("#displacedThreshold").write(tresh);
  }

  @Test
  void checkManualPanelPopUp(FxRobot robot){
      robot.clickOn("#guideBtn");
      FxAssert.verifyThat("#manual", NodeMatchers.isVisible());
  }

  @Test
  void checkContrastBtn(FxRobot robot){
    robot.clickOn("#contrastB");
    Button btn = (Button) robot.lookup(".button").queryAll().iterator().next();
    Assertions.assertTrue(btn.getStyleClass().contains("button2"));
  }

  @Test
  void checkEmailPanel(FxRobot robot) throws Exception {
    robot.clickOn("#helpButton");
    FxAssert.verifyThat("#receiver", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#subject", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#sender", NodeMatchers.isNotNull());
    FxAssert.verifyThat("#area", NodeMatchers.isNotNull());

    robot.clickOn("#receiver").write("emailTest@domain.com");
    robot.clickOn("#subject").write("test message");
    robot.clickOn("#area").write("test area");

    FxAssert.verifyThat("#receiver", NodeMatchers.hasChild("emailTest@domain.com"));
    FxAssert.verifyThat("#subject", NodeMatchers.hasChild("test message"));
    FxAssert.verifyThat("#area", NodeMatchers.hasChild("test area"));
}

  @Test
  void checkInputError(FxRobot robot) {
    fillInputInputs(robot, "test", "22", "32", "333");
    robot.clickOn("#submit");
    FxAssert.verifyThat(".error", NodeMatchers.isVisible());
    robot.closeCurrentWindow();
    if (robot.lookup(".error").tryQuery().isPresent()) {
      Assertions.fail("Window was not closed");
    }
  }

  @Test
  void checkObstaclePanel(FxRobot robot) throws InterruptedException {
    fillInputInputs(robot, "test", "22", "32", "273");
    robot.clickOn("#submit");
    FxAssert.verifyThat("#height", NodeMatchers.isVisible());
  }
}
