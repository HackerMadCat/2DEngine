package com.abstractEngine.math;

import java.util.ArrayList;
import java.util.List;

public class Figure {

    private Point pos;
    private List<Vector> vectors;
    public Point center;

    public Figure(Figure figure) {
        this.pos = figure.pos;
        this.center = figure.center;
        this.vectors = new ArrayList<>(figure.vectors);
    }

    public Figure(Point pos, Figure figure) {
        this.pos = pos;
        this.center = figure.center;
        this.vectors = new ArrayList<>(figure.vectors);
    }

    public Figure(Point pos, List<Vector> vec) throws IllegalArgumentException {
        this.pos = pos;
        if (vec.size() < 3) throw new IllegalArgumentException();
        double accuracy = 0.00001;
        Vector sum = vec.get(0);
        for (int i = 1; i < vec.size(); ++i) sum = Vector.add(sum, vec.get(i));
        if (sum.abs() > accuracy) throw new IllegalArgumentException("Figure is not closed.");
        this.vectors = new ArrayList<>(vec);
        this.center = calcCenter();
    }

    public boolean convex() {
        double direct = Vector.vectorMul(vectors.get(vectors.size() - 1), vectors.get(0));
        for (int i = 1; i < vectors.size(); ++i)
            if (0 > direct * Vector.vectorMul(vectors.get(i - 1), vectors.get(i)))
                return false;
        return true;
    }

    public double height() {
        Vector prev = new Vector(pos);
        double min = prev.y();
        double max = prev.y();
        for (Vector vector : vectors) {
            prev = Vector.add(prev, vector);
            if (prev.y() > max) max = prev.y();
            if (prev.y() < min) min = prev.y();
        }
        return Math.abs(max - min);
    }

    public double width() {
        Vector prev = new Vector(pos);
        double min = prev.x();
        double max = prev.x();
        for (Vector vector : vectors) {
            prev = Vector.add(prev, vector);
            if (prev.x() > max) max = prev.x();
            if (prev.x() < min) min = prev.x();
        }
        return Math.abs(max - min);
    }

    public boolean isInside(Point point) {
        Vector m = Vector.rem(new Vector(point), new Vector(pos));
        double direct = 0, temp = 0;
        boolean init = false;
        for (Vector vector : vectors) {
            temp = Vector.vectorMul(vector, m);
            m = Vector.rem(m, vector);
            if (temp != 0) {
                if (!init) {
                    init = true;
                } else if (direct * temp < 0)
                    return false;
                direct = temp;
            }
        }
        return true;
    }

    private Point calcCenter() {
        Point prev = pos;
        double x = prev.x;
        double y = prev.y;
        for (int i = 0; i < vectors.size() - 1; ++i) {
            prev = Point.add(prev, vectors.get(i).pos);
            x += prev.x;
            y += prev.y;
        }
        return new Point(x / vectors.size(), y / vectors.size());
    }

    public static boolean intersection(Figure _1, Figure _2) {
        Vector prev = new Vector(_1.pos());
        for (Vector vector : _1.vectors) {
            prev = Vector.add(prev, vector);
            if (_2.isInside(prev.pos)) return true;
        }
        prev = new Vector(_2.pos());
        for (Vector vector : _2.vectors) {
            prev = Vector.add(prev, vector);
            if (_1.isInside(prev.pos)) return true;
        }
        return false;
    }

    public static boolean intersection(Figure _1, Polyline _2) {
        return Polyline.intersection(_2, _1);
    }

    public static boolean intersection(Figure _1, Section _2) {
        return _1.isInside(_2._1) || _1.isInside(_2._2);
    }

    public static boolean intersection(Figure _1, Line _2) {
        Point prev = _1.pos;
        for (Vector vector : _1.vectors) {
            if (Section.intersection(new Section(prev, vector.pos), _2)) return true;
            prev = vector.pos;
        }
        return false;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point pos() {
        return pos;
    }

    public List<Vector> vectors() {
        return new ArrayList<>(vectors);
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Figure)) return false;
        Figure figure = (Figure) obj;
        return figure.vectors.equals(vectors);
    }

    @Override
    public int hashCode() {
        int result;
        result = vectors.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "[" + "'Position = " + pos + "', 'Vectors = " + vectors + "', 'Center = " + center + "']";
    }
}