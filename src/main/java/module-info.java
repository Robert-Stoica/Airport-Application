module org.comp2211 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.xml;

  opens org.comp2211 to
      javafx.fxml;

  exports org.comp2211;
  exports org.comp2211.media;
  exports org.comp2211.Calculations;

  opens org.comp2211.media to
      javafx.fxml;
}
