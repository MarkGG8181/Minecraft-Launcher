package dev.lexi.launcher.utils.swing;

import javax.swing.*;
import java.awt.*;

public class FontUtil {

    public static JLabel getLabel(String text, String fontName, int fontSize) {
        return new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font(fontName, Font.BOLD, fontSize));
                super.paintComponent(g);
            }
        };
    }

}
