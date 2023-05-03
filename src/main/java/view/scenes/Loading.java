package view.scenes;

import java.util.Objects;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Loading extends AbstractScene {


    public Loading() {
        super();
    }

    /**
     * Creates all the components of the scene, and adds them to the layout
     * Loading scene while waiting for log files to be parsed
     */
    public void createScene() {
        Label loadingLabel = new Label("Loading...");
        loadingLabel.getStyleClass().add("loading");

        var loadingPane = new StackPane();
        loadingPane.getChildren().add(loadingLabel);
        StackPane.setAlignment(loadingPane, Pos.CENTER);
        scene = new Scene(loadingPane, 1280, 720);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/view/start.css")).toExternalForm());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> loadingLabel.setText("Loading")),
                new KeyFrame(Duration.seconds(0.5), event -> loadingLabel.setText("Loading.")),
                new KeyFrame(Duration.seconds(1), event -> loadingLabel.setText("Loading..")),
                new KeyFrame(Duration.seconds(1.5), event -> loadingLabel.setText("Loading...")),
                new KeyFrame(Duration.seconds(2), event -> loadingLabel.setText("Loading"))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), loadingLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.3);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.play();
    }

    public void setStyles(boolean theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/view/start.css").toExternalForm());
//        if (theme) {
//            scene.getStylesheets().add(getClass().getResource("/view/start.css").toExternalForm());
//        } else {
//            scene.getStylesheets().add(getClass().getResource("/view/startLight.css").toExternalForm());
//        }
    }
}
