package edu.luc.etl.cs313.android.shapes.model;

/**
 * A point, implemented as a location without a shape.
 */
public class Point extends Location {
    // used a circle with radius 0 as the shape

    public Point(final int x, final int y) {
        super(x, y, new Circle(0));
    }
}
