package com.navigation.edgescreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class MyViewBG extends View {

    private OnTouchViewSeekBar onTouchViewSeekBar;

    public void setOnTouchViewSeekBar(OnTouchViewSeekBar onTouchViewSeekBar) {
        this.onTouchViewSeekBar = onTouchViewSeekBar;
    }

    private Paint paintTop, paintBottom;
    private int round, startTouch = -1, value;

    private Bitmap bmTop, bmBottom;
    private Rect rectTop, rectBottom;

    private int sizeImage;
    private Bitmap bmW, bmB;

    public void setBmImage(Bitmap bmW, Bitmap bmB) {
        this.bmW = bmW;
        this.bmB = bmB;
        invalidate();
    }

    public MyViewBG(Context context) {
        super(context);
        init();
    }

    public MyViewBG(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewBG(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintTop = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTop.setColor(Color.parseColor("#73000000"));
        paintBottom = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBottom.setColor(Color.WHITE);
        paintBottom.setAlpha(200);
        round = 20;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (rectTop == null) {
            if (startTouch == -1)
                startTouch = getHeight() - getHeight() * value / 100;
            rectTop = new Rect(0, 0, getWidth(), startTouch);
            rectBottom = new Rect(0, startTouch, getWidth(), getHeight());
            sizeImage = getHeight() / 7;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bmB != null && bmTop == null) {
            int h = getHeight() * 17 / 21;
            Rect rect = new Rect((getWidth() - sizeImage) / 2, h,
                    (getWidth() - sizeImage) / 2 + sizeImage, h + sizeImage);
            bmTop = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(bmTop);
            Path p = RoundedRect(0, 0, getWidth(), getHeight(), round, round, true, true, true, true);
            cv.drawPath(p, paintTop);
            cv.drawBitmap(bmW, null, rect, null);

            bmBottom = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmBottom);
            Path p1 = RoundedRect(0, 0, getWidth(), getHeight(), round, round, true, true, true, true);
            c.drawPath(p1, paintBottom);
            c.drawBitmap(bmB, null, rect, null);
        }

        rectTop.set(0, 0, getWidth(), startTouch);
        rectBottom.set(0, startTouch, getWidth(), getHeight());
        canvas.drawBitmap(bmTop, rectTop, rectTop, null);
        canvas.drawBitmap(bmBottom, rectBottom, rectBottom, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startTouch = (int) event.getY();
        if (startTouch < 0)
            startTouch = 0;
        else if (startTouch > getHeight())
            startTouch = getHeight();
        invalidate();
        if (onTouchViewSeekBar != null)
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchViewSeekBar.touchDown(this);
                    break;
                case MotionEvent.ACTION_MOVE:
                    onTouchViewSeekBar.touchMove(this, (getHeight() - startTouch) * 100 / getHeight());
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchViewSeekBar.touchUp(this);
                    break;
            }
        return true;
    }

    public void setProgress(int value) {
        if (startTouch == -1) {
            this.value = value;
        } else {
            startTouch = value * getHeight() / 100;
            invalidate();
        }
    }

    public static Path RoundedRect(float left, float top, float right, float bottom, float rx,
                                   float ry, boolean tl, boolean tr, boolean br, boolean bl) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else {
            path.rLineTo(0, -ry);
            path.rLineTo(-rx, 0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else {
            path.rLineTo(-rx, 0);
            path.rLineTo(0, ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else {
            path.rLineTo(0, ry);
            path.rLineTo(rx, 0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry);
        else {
            path.rLineTo(rx, 0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();

        return path;
    }

    public interface OnTouchViewSeekBar {
        void touchDown(MyViewBG v);

        void touchMove(MyViewBG v, int value);

        void touchUp(MyViewBG v);
    }
}