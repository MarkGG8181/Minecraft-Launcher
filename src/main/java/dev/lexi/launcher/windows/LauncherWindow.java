package dev.lexi.launcher.windows;

import dev.lexi.launcher.data.version.Version;
import dev.lexi.launcher.game.VersionManager;
import dev.lexi.launcher.gui.CustomButton;
import dev.lexi.launcher.gui.CustomDropdown;
import dev.lexi.launcher.init.Initializer;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LauncherWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lexi's Launcher");

        AnchorPane layout = new AnchorPane();

        String[] versions = Initializer.instance.versionList.available_releases
                .stream()
                .map(v -> v.versionId)
                .toArray(String[]::new);
        CustomDropdown customDropdown = new CustomDropdown(versions);
        javafx.scene.control.ComboBox<String> versionDropdown = customDropdown.createDropdown();
        AnchorPane.setTopAnchor(versionDropdown, 150.0);
        AnchorPane.setLeftAnchor(versionDropdown, 350.0);
        AnchorPane.setRightAnchor(versionDropdown, 350.0);

        CustomButton customButton = new CustomButton("Launch");
        Button launchButton = customButton.createButton();
        AnchorPane.setTopAnchor(launchButton, 230.0);
        AnchorPane.setLeftAnchor(launchButton, 450.0);

        launchButton.setOnAction(e -> {
            String selectedVersion = versionDropdown.getValue();
            if (selectedVersion != null) {
                Version version = Initializer.instance.versionList.getVersionById(selectedVersion);
                VersionManager.instance.launch(version);
            }
        });

        Pane animatedBackground = createAnimatedLiquidBackground();
        layout.getChildren().addAll(animatedBackground, versionDropdown, launchButton);

        Scene scene = new Scene(layout, 1000, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane createAnimatedLiquidBackground() {
        Pane backgroundPane = new Pane();
        backgroundPane.setPrefSize(1000, 400);

        Circle circle1 = createColorCircle(Color.web("#20243D"));
        Circle circle2 = createColorCircle(Color.web("#344A5F"));
        Circle circle3 = createColorCircle(Color.web("#5B7097"));
        Circle circle4 = createColorCircle(Color.web("#436B78"));

        backgroundPane.getChildren().addAll(circle1, circle2, circle3, circle4);
        backgroundPane.setEffect(new javafx.scene.effect.GaussianBlur(50));

        animateCircle(circle1, 200, 200, 800, 600, Duration.seconds(20));
        animateCircle(circle2, 400, 400, -600, 300, Duration.seconds(25));
        animateCircle(circle3, -200, -200, 1000, 800, Duration.seconds(30));
        animateCircle(circle4, 600, 100, -300, -500, Duration.seconds(35));

        return backgroundPane;
    }

    private Circle createColorCircle(Color color) {
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, color),
                new Stop(1, Color.TRANSPARENT)
        );

        Circle circle = new Circle(300);
        circle.setFill(gradient);
        return circle;
    }

    private void animateCircle(Circle circle, double startX, double startY, double endX, double endY, Duration duration) {
        circle.setTranslateX(startX);
        circle.setTranslateY(startY);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(circle.translateXProperty(), startX),
                        new KeyValue(circle.translateYProperty(), startY)
                ),
                new KeyFrame(
                        duration,
                        new KeyValue(circle.translateXProperty(), endX),
                        new KeyValue(circle.translateYProperty(), endY)
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    public static void startLauncher() {
        Application.launch(LauncherWindow.class);
    }
}
