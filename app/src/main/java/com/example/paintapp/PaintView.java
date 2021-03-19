package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PaintView extends View {
    public int BRUSH_SIZE = 20;
    public int PEN_COLOR = ContextCompat.getColor(getContext(), R.color.blue_700);
    public int ERASER_COLOR = Color.WHITE;
    public int DEFAULT_BG_COLOR = Color.WHITE;
    public int TOUCH_TOLERANCE = 4;

    private float x, y;
    private Path path;
    private Paint paint;
    private int currentColor;
    private ArrayList<FingerPath> fingerPaths = new ArrayList<>();

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bitmapPaint = new Paint(Paint.DITHER_FLAG);

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(PEN_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(null);
        paint.setAlpha(0xff);
    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public void pen() {
        currentColor = PEN_COLOR;
    }

    public void eraser() {
        currentColor = ERASER_COLOR;
    }

    public void clear() {
        fingerPaths.clear();
        pen();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawColor(DEFAULT_BG_COLOR);

        for (FingerPath fingerPath : fingerPaths) {
            paint.setColor(fingerPath.getColor());
            paint.setStrokeWidth(fingerPath.getStrokeWidth());
            paint.setMaskFilter(null);

            canvas.drawPath(fingerPath.getPath(), paint);
        }

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        path = new Path();
        FingerPath fingerPath = new FingerPath(currentColor, BRUSH_SIZE, path);
        fingerPaths.add(fingerPath);

        path.reset();
        path.moveTo(x, y);
        this.x = x;
        this.y = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - this.x);
        float dy = Math.abs(y - this.y);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(this.x, this.y, (x + this.x) / 2, (y + this.y) / 2);
            this.x = x;
            this.y = y;
        }
    }

    private void touchUp() {
        path.lineTo(this.x, this.y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }

        return true;
    }
}
