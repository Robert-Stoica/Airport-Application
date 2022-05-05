import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.comp2211.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.robot.Motion;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ApplicationExtension.class)
public class IntegrationTesting {

  App app = new App();

  @Start
  public void start(Stage stage) throws IOException {
    Parent rootNode = FXMLLoader.load(getClass().getResource("Input.fxml"));
    stage.setTitle("InputScene Test");
    stage.setScene(new Scene(rootNode));
    stage.toFront();
    stage.show();
    app.start(stage);
    app.runwayInput();
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
    stage.setY(0);

  }

  @AfterAll
  static void tearDown() throws TimeoutException {
    FxToolkit.hideStage();
    FxToolkit.cleanupStages();
  }


  @Test
  @Order(8)
  void Scenario(FxRobot robot) throws InterruptedException {
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
    fillObstacleInputs(robot,"12","0","-50");
    robot.clickOn("#submit");
    robot.closeCurrentWindow();
    robot.moveTo(0,10,Motion.VERTICAL_FIRST);
    robot.clickOn("#menu");
    robot.type(KeyCode.DOWN);
    robot.type(KeyCode.ENTER);
  }

  void fillObstacleInputs(FxRobot robot, String height, String center, String tresh) {
    robot.clickOn("#height").write(height);
    robot.clickOn("#centre").write(center);
    robot.clickOn("#threshold").write(tresh);
  }

    @Test
    @Order(1)
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
    @Order(3)
    void checkManualPanelPopUp(FxRobot robot) {
      robot.clickOn("#guideBtn");
      FxAssert.verifyThat("#manual", NodeMatchers.isEnabled());
      robot.closeCurrentWindow();
    }


    @Test
    @Order(2)
    void checkContrastBtn(FxRobot robot) {
      robot.clickOn("#contrastB");
      Node btn =  robot.lookup("#submit").queryAll().iterator().next();
      assertTrue(btn.getStyleClass().contains("button2"));
    }

    @Test
    @Order(4)
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

      robot.clickOn("#sender");
      robot.closeCurrentWindow();

    }

    @Test
    @Order(5)
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

    @Test
    @Order(6)
    void checkObstacleInput(FxRobot robot){
      fillInputInputs(robot, "test", "22", "32", "223");
      robot.clickOn("#submit");
      robot.closeCurrentWindow();
      FxAssert.verifyThat("#height", NodeMatchers.isVisible());
    }

  @Test
  @Order(7)
  void checkVisualRunway(FxRobot robot){
    fillInputInputs(robot, "09L", "3902", "3595", "306");
    robot.clickOn("#submit");
    robot.closeCurrentWindow();
    robot.closeCurrentWindow();
    fillObstacleInputs(robot, "12","0","-50");
    robot.clickOn("#menu");
    robot.type(KeyCode.DOWN);
    robot.type(KeyCode.ENTER);
    robot.clickOn("#submit");
    robot.closeCurrentWindow();
    FxAssert.verifyThat("#calculation", NodeMatchers.isVisible());
  }

}
