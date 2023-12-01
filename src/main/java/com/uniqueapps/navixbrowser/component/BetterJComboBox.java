package com.uniqueapps.navixbrowser.component;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BetterJComboBox<E> extends JComboBox<E> {


    public BetterJComboBox() {
        super();
    }

    public BetterJComboBox(E[] eArray) {
        super(eArray);
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
