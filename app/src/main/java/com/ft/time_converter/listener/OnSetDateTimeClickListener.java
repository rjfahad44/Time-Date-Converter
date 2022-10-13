package com.ft.time_converter.listener;

import android.view.View;

import com.ft.time_converter.modles.TimeZoneListModel;

public interface OnSetDateTimeClickListener {
    void onSetDateTimeClick(View view, TimeZoneListModel model, int position);
}
