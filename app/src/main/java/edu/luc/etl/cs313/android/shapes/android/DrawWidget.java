package edu.luc.etl.cs313.android.shapes.android;

import edu.luc.etl.cs313.android.shapes.model.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawWidget extends View {

    private final Paint paint = new Paint();

    public DrawWidget(final Context context, final AttributeSet attrs, final int defStyle) {
        this(context);
    }

    public DrawWidget(final Context context, final AttributeSet attrs) {
        this(context);
    }

    public DrawWidget(final Context context) { super(context); }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
    
    // TODO once BoundingBox and Draw are implemented, change Fixtures.simpleCircle
    // to Fixtures.complexGroup and test the app on an emulator or Android device
    // to make sure the correct figure is drawn (see Project 3 description for link)

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(final Canvas canvas) {
        //creates circle
        // var shape = Fixtures.simpleStrokeColor;
         //var shape = Fixtures.simpleFill;
        // var shape = Fixtures.simpleGroup;
        //var shape = Fixtures.simpleLocation;
        //var shape = Fixtures.simplePolygon;
        //var shape = Fixtures.simpleOutline;
        var shape = Fixtures.complexGroup; // the circle is filled instead of outlined, box is also filled.
        final var b = shape.accept(new BoundingBox()); //creates the bounding box of the circle
        canvas.translate(-b.getX(), -b.getY()); //coordinates
        b.accept(new Draw(canvas, paint)); //
        shape.accept(new Draw(canvas, paint));
        canvas.translate(b.getX(), b.getY());
    }
}
