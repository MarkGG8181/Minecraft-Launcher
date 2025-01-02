package dev.lexi.launcher.gui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class CustomButton {

    private final String buttonText;

    public CustomButton(String buttonText) {
        this.buttonText = buttonText;
    }

    public Button createButton() {
        Button button = new Button(buttonText);
        button.setFont(Font.font("SansSerif", 16));
        button.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #3c3c3c; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 16; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 3);");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #2d2d2d; -fx-border-color: #3c3c3c; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 16; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 3);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #1e1e1e; -fx-border-color: #3c3c3c; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 16; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 3);"));
        button.setOnMousePressed(e -> button.setStyle("-fx-background-color: #3c3c3c; -fx-border-color: #3c3c3c; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 16; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 3);"));
        return button;
    }
}
