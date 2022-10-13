package com.ft.time_converter.text_Clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextClock;

public class MyTextClock extends TextClock {
    public MyTextClock(Context context) {
        super(context);

        this.setDesigningText();
    }

    public MyTextClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDesigningText();
    }

    public MyTextClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setDesigningText();
    }

    public MyTextClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setDesigningText();
    }

    @SuppressLint("SetTextI18n")
    private void setDesigningText() {
        this.setText("11:30:00");
    }

    @Override
    protected void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        }catch (Exception e){
        }

    }
}
