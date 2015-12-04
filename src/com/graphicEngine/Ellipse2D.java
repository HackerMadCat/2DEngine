package com.graphicEngine;

import com.abstractEngine.math.*;
import com.abstractEngine.math.Point;

import java.awt.*;

public class Ellipse2D implements View {

    public final Ellipse ellipse;
    private Color outline;

    public Ellipse2D(Ellipse ellipse, Color outline) {
        this.ellipse = ellipse;
        this.outline = outline;
    }

    public Color outline() {
        return outline;
    }

    public void set(Color outline) {
        this.outline = outline;
    }

    @Override
    public void show(Graphics graphics, Point posScreen, int height, int width) {
        graphics.setColor(outline);
        Vector size = new Vector(ellipse.width(), ellipse.height());
        Point temp = posScreen.add(ellipse.pos()).rem(size.mul(1.0/2));
        graphics.drawOval((int)(temp.x), (int)(temp.y), (int)(size.x), (int)(size.y));
    }
}