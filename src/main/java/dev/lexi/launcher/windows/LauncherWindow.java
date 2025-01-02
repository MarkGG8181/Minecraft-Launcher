package dev.lexi.launcher.windows;

import dev.lexi.launcher.game.VersionManager;
import dev.lexi.launcher.gui.CustomButton;
import dev.lexi.launcher.gui.CustomDropdown;
import dev.lexi.launcher.init.Initializer;
import dev.lexi.launcher.gui.CustomBackground;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LauncherWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Lexi's Launcher");
        primaryStage.setResizable(false);

        AnchorPane layout = new AnchorPane();
        Pane background = new CustomBackground().createBackground();

        String[] versions = Initializer.instance.versionList.available_releases.stream().map(v -> v.versionId).toArray(String[]::new);

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
            if (selectedVersion != null) VersionManager.instance.launch(Initializer.instance.versionList.getVersionById(selectedVersion));
        });

        layout.getChildren().addAll(background, versionDropdown, launchButton);
        primaryStage.setScene(new Scene(layout, 1000, 400));
        primaryStage.show();
    }

    public static void startLauncher() {
        Application.launch(LauncherWindow.class);
    }
}
