package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;
    private final Paint paint;
    private final Paint ogPaint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
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
        paint.setStyle(Style.FILL_AND_STROKE);
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
        final int x = l.getX();
        final int y = l.getY();
        canvas.translate(x, y);
        l.getShape().accept(this);
        canvas.translate(-x, -y);
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(final Outline o) {
        final Style oldStyle = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        int numPoints = s.getPoints().size();
        if (numPoints < 2) return null;

        float[] pts = new float[numPoints * 4];
        int index = 0;

        for (int i = 0; i < numPoints - 1; i++) {
            Point p1 = s.getPoints().get(i);
            Point p2 = s.getPoints().get(i + 1);
            pts[index++] = p1.getX();
            pts[index++] = p1.getY();
            pts[index++] = p2.getX();
            pts[index++] = p2.getY();
        }

        if (numPoints > 1) {
            Point first = s.getPoints().get(0);
            Point last = s.getPoints().get(numPoints - 1);
            pts[index++] = last.getX();
            pts[index++] = last.getY();
            pts[index++] = first.getX();
            pts[index++] = first.getY();
        }

        canvas.drawLines(pts, paint);
        return null;
    }
}
