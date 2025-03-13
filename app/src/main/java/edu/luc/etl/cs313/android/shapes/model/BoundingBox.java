package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        if (g.getShapes().isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape s : g.getShapes()) {
            Location l = s.accept(this);
            int shapeMinX = l.getX();
            int shapeMinY = l.getY();
            int shapeMaxX = shapeMinX + ((Rectangle) l.getShape()).getWidth();
            int shapeMaxY = shapeMinY + ((Rectangle) l.getShape()).getHeight();

            minX = Math.min(minX, shapeMinX);
            minY = Math.min(minY, shapeMinY);
            maxX = Math.max(maxX, shapeMaxX);
            maxY = Math.max(maxY, shapeMaxY);
        }

        final int width = maxX - minX;
        final int height = maxY - minY;
        return new Location(minX, minY, new Rectangle(width, height));
    }

    @Override
    public Location onLocation(final Location l) {
        final Location boundingBox = l.getShape().accept(this);
        final int newX = l.getX() + boundingBox.getX();
        final int newY = l.getY() + boundingBox.getY();

        return new Location(newX, newY, boundingBox.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        final int halfWidth = r.getWidth() / 2;
        final int halfHeight = r.getHeight() / 2;
        //update: i changed the x and y values to 0. The test expects it to be in location 0/0. IDK if
        //impacts other tests as well. I'll keep testing.
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
        //return new Location(-halfWidth, -halfHeight, new Rectangle(r.getWidth(), r.getHeight()));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : s.getPoints()) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        final int width = maxX - minX;
        final int height = maxY - minY;

        return new Location(minX, minY, new Rectangle(width, height));
    }
}
