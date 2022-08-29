package com.uniqueapps.navixbrowser.component;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedBorder extends AbstractBorder {

	private static final long serialVersionUID = -4780079987980303498L;
	Color color;
    Stroke stroke;
    Insets borderInsets;
    int radius;

    public RoundedBorder(Color color, int thickness, int radius, int insets) {
        this.color = color;
        this.stroke = new BasicStroke(thickness);
        int borderSize = thickness + insets;
        this.borderInsets = new Insets(borderSize, borderSize + 2, borderSize, borderSize + 2);
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(stroke);
        g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return borderInsets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }
}
