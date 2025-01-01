package dev.lexi.launcher.windows;

import dev.lexi.launcher.data.version.Version;
import dev.lexi.launcher.game.VersionManager;
import dev.lexi.launcher.init.Initializer;
import dev.lexi.launcher.utils.swing.FontUtil;

import javax.swing.*;
import java.awt.*;

public class LauncherWindow {

    public static void init() {
        JFrame frame = new JFrame();

        frame.setSize(700, 300);
        frame.setTitle("Lexi's launcher");

        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JLabel jLabel = FontUtil.getLabel("Current version: ", "Arial", 12);
        jLabel.setSize(jLabel.getPreferredSize());
        jLabel.setLocation(5, 300 - 30 - 44 - 20);
        JComboBox<String> versionDropdown = createVersionDropdown();

        versionDropdown.setBounds(5, 300 - 30 - 44, 200, 30);

        JButton mainButton = new JButton("Launch");
        mainButton.setFont(Font.getFont("Arial"));
        mainButton.setSize(100, 30);
        mainButton.setLocation(700 - 100 - 20, 300 - 30 - 44);

        frame.add(mainButton);
        frame.add(jLabel);
        frame.add(versionDropdown);

        mainButton.addActionListener((e) -> {
            String id = (String) versionDropdown.getSelectedItem();
            Version version = Initializer.instance.versionList.getVersionById(id);

            VersionManager.instance.launch(version);
        });

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static JComboBox<String> createVersionDropdown() {

        String[] versionTags = new String[Initializer.instance.versionList.available_releases.size()];
        for (int i = 0; i < Initializer.instance.versionList.available_releases.size(); i++) {
            versionTags[i] = Initializer.instance.versionList.available_releases.get(i).versionId;
        }

        return new JComboBox<>(versionTags);
    }

}
