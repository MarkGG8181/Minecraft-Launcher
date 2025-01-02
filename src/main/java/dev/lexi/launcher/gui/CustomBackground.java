package dev.lexi.launcher.gui;

import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.util.Duration;

import java.util.Random;

public class CustomBackground {

    public Pane createBackground() {
        Pane backgroundPane = new Pane();
        backgroundPane.setPrefSize(1000, 400);
        backgroundPane.setStyle("-fx-background-color: #333333;");

        for (int i = 0; i < 50; i++) {
            javafx.scene.shape.Circle firefly = createGlowingFirefly();
            backgroundPane.getChildren().add(firefly);
            animateFirefly(firefly);
        }

        return backgroundPane;
    }

    private javafx.scene.shape.Circle createGlowingFirefly() {
        javafx.scene.shape.Circle firefly = new javafx.scene.shape.Circle(5, Color.YELLOW);
        firefly.setOpacity(0.7);

        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow(20, Color.YELLOW);
        glow.setSpread(0.5);
        firefly.setEffect(glow);

        firefly.setTranslateX(new Random().nextDouble() * 1000);
        firefly.setTranslateY(new Random().nextDouble() * 400);
        return firefly;
    }

    private void animateFirefly(javafx.scene.shape.Circle firefly) {
        Random random = new Random();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(5 + random.nextDouble() * 5), firefly);
        translateTransition.setByX(random.nextDouble() * 200 - 100);
        translateTransition.setByY(random.nextDouble() * 200 - 100);
        translateTransition.setCycleCount(Animation.INDEFINITE);
        translateTransition.setAutoReverse(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2 + random.nextDouble() * 2), firefly);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2 + random.nextDouble() * 2), firefly);
        scaleTransition.setFromX(0.7);
        scaleTransition.setFromY(0.7);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);

        ParallelTransition fireflyAnimation = new ParallelTransition(translateTransition, fadeTransition, scaleTransition);
        fireflyAnimation.play();
    }
}
