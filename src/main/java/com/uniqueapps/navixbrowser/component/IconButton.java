package com.uniqueapps.navixbrowser.component;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IconButton extends JButton {

    public static Font segoeFluentIcons;

    static {
        try {
            segoeFluentIcons = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(IconButton.class.getResourceAsStream("/fonts/segoe-fluent-icons.ttf")));
            segoeFluentIcons = segoeFluentIcons.deriveFont(Font.BOLD, 16f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IconButton(char unicodeIcon) {
        setFont(segoeFluentIcons);
        setText(String.valueOf(unicodeIcon));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Map<RenderingHints.Key, Object> rh = new HashMap<>();
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        super.paintComponent(g2d);
    }
}
