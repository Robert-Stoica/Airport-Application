package org.comp2211;

import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController {

  @FXML
  private void switchToPrimary() throws IOException {
    App.setRoot("primary");
  }
}
