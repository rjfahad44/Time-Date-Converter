package com.ft.time_converter.listener;

import android.view.View;

import com.ft.time_converter.modles.TimeZoneSearchListModel;

public interface OnItemClickListener {
    void onItemClick(View view, TimeZoneSearchListModel model, int position);
}
