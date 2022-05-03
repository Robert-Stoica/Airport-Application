import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.comp2211.App;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
public class IntegrationTesting {

  App app = new App();

  @Start
  public void start(Stage stage) throws IOException {
    Parent rootNode = FXMLLoader.load(getClass().getResource("Input.fxml"));
    stage.setTitle("InputScene Test");
    stage.setScene(new Scene(rootNode));
    stage.show();
    stage.toFront();
    app.start(stage);
    app.runwayInput();
  }

  @AfterAll
  static void tearDown() throws Exception{
    FxToolkit.cleanupStages();
    FxToolkit.hideStage();

  }

  @Test
  void Scenario_1(FxRobot robot) throws InterruptedException {
    robot.clickOn("#addpreset");
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection stringSelection = new StringSelection("09L");
    clipboard.setContents(stringSelection, stringSelection);
    robot.press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
    robot.push(KeyCode.ENTER);
    robot.closeCurrentWindow();
    robot.clickOn("#submit");
    robot.closeCurrentWindow();
    robot.closeCurrentWindow();
    robot.clickOn("#menu");
    robot.type(KeyCode.DOWN);
    robot.type(KeyCode.ENTER);
  }

  /*
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

    void fillObstacleInputs(FxRobot robot, String height, String center, String tresh) {
      robot.clickOn("#height").write(height);
      robot.clickOn("#centre").write(center);
      robot.clickOn("#threshold").write(tresh);
    }

    @Test
    void checkManualPanelPopUp(FxRobot robot) {
      robot.clickOn("#guideBtn");
      FxAssert.verifyThat("#manual", NodeMatchers.isEnabled());
      robot.closeCurrentWindow();
    }


    @Test
    void checkContrastBtn(FxRobot robot) {
      robot.clickOn("#contrastB");
      Button btn = (Button) robot.lookup("#exportXml").queryAs(Button.class);
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
      FxAssert.verifyThat(".information", NodeMatchers.isVisible());
      robot.closeCurrentWindow();
    }
  */

}
