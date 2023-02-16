package com.uniqueapps.navixbrowser.component;

import java.awt.geom.Path2D;

public class TopEnd extends Path2D.Float {

    public TopEnd(float width, float height, float radius) {
        moveTo(0, height);
        lineTo(0, radius);
        curveTo(0, 0, 0, 0, radius, 0);
        lineTo(width - radius, 0);
        curveTo(width, 0, width, 0, width, radius);
        lineTo(width, height);
        closePath();
    }
}