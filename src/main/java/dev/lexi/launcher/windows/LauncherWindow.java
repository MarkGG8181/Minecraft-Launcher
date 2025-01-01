package dev.lexi.launcher.windows;

import dev.lexi.launcher.data.version.Version;
import dev.lexi.launcher.game.VersionManager;
import dev.lexi.launcher.gui.CustomButton;
import dev.lexi.launcher.gui.CustomDropdown;
import dev.lexi.launcher.init.Initializer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
        AnchorPane.setTopAnchor(versionDropdown, 10.0);
        AnchorPane.setLeftAnchor(versionDropdown, 20.0);
        AnchorPane.setRightAnchor(versionDropdown, 20.0);

        CustomButton customButton = new CustomButton("Launch");
        Button launchButton = customButton.createButton();
        AnchorPane.setBottomAnchor(launchButton, 20.0);
        AnchorPane.setLeftAnchor(launchButton, 20.0);

        launchButton.setOnAction(e -> {
            String selectedVersion = versionDropdown.getValue();
            if (selectedVersion != null) {
                Version version = Initializer.instance.versionList.getVersionById(selectedVersion);
                VersionManager.instance.launch(version);
            }
        });

        layout.getChildren().addAll(versionDropdown, launchButton);

        Scene scene = new Scene(layout, 1000, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void startLauncher() {
        Application.launch(LauncherWindow.class);
    }
}
