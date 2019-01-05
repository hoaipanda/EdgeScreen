package com.navigation.edgescreen.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MarqueeTextView extends AppCompatTextView {

    private boolean isStop = false;

    public MarqueeTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }

    public boolean isFocused() {
        if (this.isStop) {
            return super.isFocused();
        }
        return true;
    }

    private void setTypeface(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SF-UI-DISPLAY-LIGHT.OTF"));
    }

    public void stopScroll() {
        this.isStop = true;
    }

    public void start() {
        this.isStop = false;
    }

    protected void onDetachedFromWindow() {
        stopScroll();
        super.onDetachedFromWindow();
    }
}