package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;
import java.util.List;
/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    private final Paint ogPaint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXED
        this.paint = paint; // FIXED
        this.ogPaint = new Paint(paint);
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        final int oldColor = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(oldColor);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        final Style oldStyle = paint.getStyle();
        paint.setStyle(Style.FILL);
        f.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        //canvas.restore();
        canvas.translate(-l.getX(), -l.getY());//to restore it, try and flip the coordinates instead of using restore. This fixed the
        //simple location test
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        final int halfWidth = r.getWidth() / 2;
        final int halfHeight = r.getHeight() / 2;
        //i changed x and y to 0. It makes a more clear rectangle,becuase the previous code placed the
        // rectangle outside the screen. It also passes the tests, not sure haha. I also removed the half stuff.
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        final Paint.Style originalStyle = paint.getStyle();

        paint.setStyle(Paint.Style.STROKE);


        o.getShape().accept(this);


        paint.setStyle(originalStyle);

        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {

        final List<? extends Point> points = s.getPoints();
        final int numPoints = points.size();


        final float[] pts = new float[numPoints * 4];

        int index = 0;
        for (int i = 0; i < numPoints; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % numPoints);

            pts[index++] = p1.getX();
            pts[index++] = p1.getY();
            pts[index++] = p2.getX();
            pts[index++] = p2.getY();
        }

        canvas.drawLines(pts, paint);
        return null;
    }
}
