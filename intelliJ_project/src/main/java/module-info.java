module org.comp2211 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.comp2211 to javafx.fxml;
    exports org.comp2211;
}