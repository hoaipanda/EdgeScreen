package com.navigation.edgescreen.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class DrawViewRight extends ViewGroup implements View.OnTouchListener {

    private Context context;
    private int heigh = 0;

    private Path path;
    private Paint paint;
    private float left0 = 0, left1 = 0, left2 = 0;
    private float center = 0;
    private float dx;
    private Bitmap iconActionQuick, iconActionHold, iconAction = null;
    private boolean isFirst;

    private enum ACTION_SWIPE {NONE, QUICK, HOLD}

    private ACTION_SWIPE actionSwipe = ACTION_SWIPE.NONE;

    public DrawViewRight(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DrawViewRight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DrawViewRight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        this.setOnTouchListener(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        heigh = displayMetrics.heightPixels;


        setWillNotDraw(false);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!isFirst) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(40, -1);
            setLayoutParams(layoutParams);
            isFirst = true;
        }
        if (center != 0 && center > 350) {
            path = new Path();

            path.moveTo(150, center - 350);
            path.cubicTo(150, center - 350, 150 - left0, center - 250, 150 - left1, center - 150);
            path.cubicTo(150 - left1, center - 150, 150 - left2 - 10, center, 150 - left1, center + 150);
            path.cubicTo(150 - left1, center + 150, 150 - left0, center + 250, 150, center + 350);
            path.close();

            canvas.drawPath(path, paint);

        }

        if (iconAction != null && center > 350) {
            iconAction = Bitmap.createScaledBitmap(iconAction, 40, 40, false);
            canvas.drawBitmap(iconAction, 150 - left1 - 20, center - iconAction.getHeight() / 2, null);
        }

    }

//    private void getBimapActionQuick() {
//        String actionquick = SharePreRightQuick.getInstance(context).getAction();
//        if (actionquick.contains("com.")) {
//            try {
//                Drawable icon = context.getPackageManager().getApplicationIcon(actionquick);
//                iconActionQuick = ((BitmapDrawable) icon).getBitmap();
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else {
//            String icon = AppUtil.getLisActionHash().get(actionquick);
//            iconActionQuick = AppUtil.getBitmapFromAsset(context, "iconaction/" + icon);
//        }
//    }

//    private void getBimapActionHold() {
//        String actionquick = SharePreRightHold.getInstance(context).getAction();
//        if (actionquick.contains("com.")) {
//            try {
//                Drawable icon = context.getPackageManager().getApplicationIcon(actionquick);
//                iconActionHold = ((BitmapDrawable) icon).getBitmap();
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        } else {
//            String icon = AppUtil.getLisActionHash().get(actionquick);
//            iconActionHold = AppUtil.getBitmapFromAsset(context, "iconaction/" + icon);
//        }
//    }

    public OnActionRight onActionRight;


    public interface OnActionRight {
        void onActionHoldRight();

        void onActionQuickRight();
    }

    public void setOnAction(OnActionRight onActionRight) {
        this.onActionRight = onActionRight;
    }


    private int mTouchSlop;
    private boolean mIsScrolling;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll.
            mIsScrolling = false;
            return false; // Do not intercept touch event, let the child handle it
        }
        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (mIsScrolling) {
                    return true;
                }

                final int xDiff = (int) ev.getX();
                if (xDiff < getWidth() / 2) {
                    mIsScrolling = true;
                    return true;
                }
                break;
            }
        }
        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        int x = (int) event.getX();
        int y = (int) event.getY();

        invalidate();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            boolean a = x < 150;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, -1);
            setLayoutParams(layoutParams);

            dx = event.getX();
            center = event.getY();

//            getBimapActionHold();
//            getBimapActionQuick();

            return a;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            if (center > 350) {
                float distance = dx - event.getX();

                distance = distance / 3;
                if (distance > 230) {
                    distance = 170;
                    actionSwipe = ACTION_SWIPE.HOLD;
                    iconAction = iconActionHold;
                } else if (distance > 50 && distance <= 150) {
                    actionSwipe = ACTION_SWIPE.QUICK;
                    iconAction = iconActionQuick;
                } else if (distance > 150 && distance < 230) {
                    distance = 150;
                    actionSwipe = ACTION_SWIPE.QUICK;
                    iconAction = iconActionQuick;
                } else {
                    iconAction = null;
                    actionSwipe = null;
                }
                left1 = distance / 2.5f;
                left0 = left1 / 25f;
                left2 = distance;
            }


        }
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(40, -1);
            setLayoutParams(layoutParams);
            center = dx = left0 = left1 = left2 = 0;
            iconAction = null;

            if (onActionRight != null) {
                if (actionSwipe == ACTION_SWIPE.QUICK) {
                    onActionRight.onActionQuickRight();
                } else if (actionSwipe == ACTION_SWIPE.HOLD) {
                    onActionRight.onActionHoldRight();
                }
            }
        }

        return false;
    }

    private float pxFromDp(Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private void drawBitmap(Bitmap bitmap) {
        Bitmap rs = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rs);
        canvas.drawBitmap(bitmap, 200, 200, null);
        Log.e("hoaiii", bitmap.getHeight() + "");
    }
}