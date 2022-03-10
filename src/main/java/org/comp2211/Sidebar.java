package org.comp2211;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Sidebar extends VBox {
  private Boolean visible = true;
  private Button eport;
  private Button iport;
  private TextField text;
  private Label label;
  private int width = 250;

    public Sidebar(){
        this.setPrefWidth(width);
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(10, 10,10,10));
        build();

    }

    public void build() {
    VBox vbox = new VBox();
    vbox.setSpacing(20);
    vbox.setPadding(new Insets(10, 10, 10, 10));

    eport = new Button("Export XML");
    iport = new Button("Import XML");
    vbox.getChildren().addAll(eport, iport);

    label = new Label("Blast Protection (m)");
    text = new TextField("Blast Protection");
    vbox.getChildren().add(text);

    this.getChildren().add(vbox);
  }

  public void toggleSidebar() {
    if (visible) {
      visible = false;
      eport.setVisible(false);
      iport.setVisible(false);
      text.setVisible(false);
      label.setVisible(false);
      Duration duration = Duration.millis(500);
      Timeline timeline =
          new Timeline(
              new KeyFrame(
                  duration, new KeyValue(this.prefWidthProperty(), 50, Interpolator.EASE_BOTH)));
      timeline.play();
    } else {
      visible = true;
      Duration duration = Duration.millis(500);
      int width = 250;
      Timeline timeline =
          new Timeline(
              new KeyFrame(
                  duration, new KeyValue(this.prefWidthProperty(), width, Interpolator.EASE_BOTH)));
      timeline.play();
      timeline.setOnFinished(
          (e) -> {
            eport.setVisible(true);
            iport.setVisible(true);
            text.setVisible(true);
            label.setVisible(true);
          });
    }
  }
}
