package dev.lexi.launcher.windows;

import javax.swing.*;

public class LauncherWindow {

    public static void init() {
        JFrame frame = new JFrame();

        frame.setSize(700, 300);
        frame.setTitle("Lexi's launcher");

        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
