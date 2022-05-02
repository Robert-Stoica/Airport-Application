module org.comp2211 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.xml;
  requires javafx.graphics;
  requires org.apache.logging.log4j;
    requires java.mail;
    requires java.desktop;

    opens org.comp2211 to
      javafx.fxml;

  exports org.comp2211;
  exports org.comp2211.media;
  exports org.comp2211.calculations;

  opens org.comp2211.media to
      javafx.fxml;
}
