package com.ft.time_converter.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.ft.time_converter.R;
import com.ft.time_converter.activity.MainActivity;
import com.ft.time_converter.listener.OnItemDeleteClickListener;
import com.ft.time_converter.listener.OnSetDateTimeClickListener;
import com.ft.time_converter.modles.TimeZoneListModel;
import com.ft.time_converter.text_Clock.MyTextClock;
import com.ft.time_converter.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TimeDateRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> zoneList = new ArrayList<>();
    private final Context context;
    private final OnItemDeleteClickListener listener;
    private final OnSetDateTimeClickListener setListener;

    private boolean isTimeZone;
    private boolean isHoliday;
    private boolean isCurrentTime;
    private boolean isTimeDifference;
    private boolean isTimeDateFormat;

    private static final int ITEM_TYPE_COUNTRY = 0;
    private static final int ITEM_TYPE_BANNER_AD = 1;

    public TimeDateRecyclerViewAdapter(Context context, OnItemDeleteClickListener listener, OnSetDateTimeClickListener setListener) {
        this.context = context;
        this.listener = listener;
        this.setListener = setListener;
    }

    public void setTimeZone(boolean timeZone) {
        isTimeZone = timeZone;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public void setCurrentTime(boolean currentTime) {
        isCurrentTime = currentTime;
    }

    public void setTimeDifference(boolean timeDifference) {
        isTimeDifference = timeDifference;
    }

    public void setTimeDateFormat(boolean timeDateFormat) {
        isTimeDateFormat = timeDateFormat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_items, parent, false);
//        return new ViewHolder(view);
        switch (viewType) {
            case ITEM_TYPE_BANNER_AD:
                //Inflate ad banner container
                View bannerLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_row, parent, false);

                //Create View Holder
                AdsViewHolder adsViewHolder = new AdsViewHolder(bannerLayoutView);
                return adsViewHolder;

            case ITEM_TYPE_COUNTRY:
            default:
                //Inflate RecyclerView row
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_view_items, parent, false);

                //Create View Holder
                final ZoneViewHolder zoneViewHolder = new ZoneViewHolder(view);

                return zoneViewHolder;
        }
    }

    @SuppressLint({"NewApi", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        //Log.d("COUNT", "\t position : "+position + "\tMod : " + (position % MainActivity.ITEMS_PER_AD));
        switch (viewType) {
            case ITEM_TYPE_BANNER_AD:
                if (zoneList.get(position) instanceof AdView) {
                    AdsViewHolder bannerHolder = (AdsViewHolder) holder;
                    AdView adView = (AdView) zoneList.get(position);
                    //ViewGroup linearLayout = (ViewGroup) bannerHolder.itemView;
                    LinearLayout linearLayout = (LinearLayout) bannerHolder.itemView;
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    if (linearLayout.getChildCount() > 0) {
                        linearLayout.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }
                    // Add the banner ad to the ad view.
                    linearLayout.addView(adView);
                    if (Utils.isNetworkConnected(context)) {
                        linearLayout.setVisibility(View.VISIBLE);
                        linearLayout.setPadding(0, 20, 0, 20);
                        layoutParams.setMargins(30, 28, 30, 0);
                        linearLayout.setLayoutParams(layoutParams);
                        //Log.d("TAG", "Active ");
                    } else {
                        linearLayout.setVisibility(View.GONE);
                        linearLayout.setPadding(0, 0, 0, 0);
                        layoutParams.setMargins(0, 0, 0, 0);
                        //Log.d("TAG", "InActive ");
                    }
                }
                break;

            case ITEM_TYPE_COUNTRY:
            default:
                if (zoneList.get(position) instanceof TimeZoneListModel) {
                    ZoneViewHolder zoneViewHolder = (ZoneViewHolder) holder;
                    TimeZoneListModel model = (TimeZoneListModel) zoneList.get(position);
                    //Log.d("TEST", model.getCity());

                    if (model.getCity().isEmpty()) {
                        String text = "<font color=#0f8766>" + model.getTitle() + "</font><font color=#414042>" + ", " + model.getCode() + "</font>";
                        zoneViewHolder.tvCityId.setText(Html.fromHtml(text));

                    } else {
                        String text = "<font color=#0f8766>" + model.getCity() + "</font><font color=#414042>" + ", " + model.getCountry() + "</font>";
                        zoneViewHolder.tvCityId.setText(Html.fromHtml(text));
                    }

                    zoneViewHolder.tvLocalUtcTimeZoneId.setText(model.getCode());
                    zoneViewHolder.tvUtcTimeZoneId.setText("(UTC " + model.getUtc_offset() + ")");
                    zoneViewHolder.tvDateId.setText(Utils.getZoneCurrentDate(model.getTimezone(), context));

                    zoneViewHolder.tvUtcTimeDifferenceId.setText(Utils.getDifferenceFromUtcInHoursAndMinutes(model.getUtc_offset(), context));

                    if (model.getIso2().isEmpty()) {
                        //zoneViewHolder.country_flag.setImageResource(R.drawable.exclamation_mark);
                        zoneViewHolder.country_flag.setImageResource(android.R.color.transparent);
                    } else {
                        try {
                            InputStream inputstream = context.getAssets().open("flags_png/" + model.getIso2().toLowerCase() + ".png");
                            Drawable drawable = Drawable.createFromStream(inputstream, null);
                            zoneViewHolder.country_flag.setImageDrawable(drawable);
                            drawable = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //Day or Night Checker//
                    if (Utils.isDayOrNight(model.getTimezone(), model.getUtc_offset(), context)) {
                        zoneViewHolder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.day));
                        zoneViewHolder.imgDayNight.setImageResource(R.drawable.ic_sun_icon);
                    } else {
                        zoneViewHolder.relativeLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.night));
                        zoneViewHolder.imgDayNight.setImageResource(R.drawable.ic_moon_icon);
                    }

                    //Work State Checker//
                    String find = Utils.dayOfWeek(context, model.getTimezone());
                    int i = model.getWeek_end().indexOf(find);
                    //Log.d("TEST", ""+i+ "\t"+find);
                    if (i >= 0) {
                        //Log.d("TEST", ""+i+ "\t"+model.getWeek_end().substring(i, i+find.length()));
                        zoneViewHolder.tvWorkState.setText("Not So Good");
                        zoneViewHolder.tvWorkState.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.not_so_good_work_state_color));
                    } else {
                        if (model.getWeek_end().equals(Utils.dayOfWeek(context, model.getTimezone()).toLowerCase())) {
                            zoneViewHolder.tvWorkState.setText("Not So Good");
                            zoneViewHolder.tvWorkState.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.not_so_good_work_state_color));
                        } else {
                            if (Utils.isWorkState(model.getTimezone(), model.getUtc_offset(), context) == 1) {
                                zoneViewHolder.tvWorkState.setText("Good");
                                zoneViewHolder.tvWorkState.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.good_work_state_color));
                            } else if (Utils.isWorkState(model.getTimezone(), model.getUtc_offset(), context) == 2) {
                                zoneViewHolder.tvWorkState.setText("Not So Good");
                                zoneViewHolder.tvWorkState.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.not_so_good_work_state_color));
                            } else {
                                zoneViewHolder.tvWorkState.setText("Not Good");
                                zoneViewHolder.tvWorkState.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.not_good_work_state_color));
                            }
                        }
                    }

                    //1st City/Zone Checker//
                    SharedPreferences prefs = context.getSharedPreferences("FIRST_CITY", MODE_PRIVATE);
                    String name = prefs.getString("name", "");
                    String firstZoneUTC = prefs.getString("utc", "");
                    String diff = "";

                    if (!model.getCity().equals(name)) {
                        diff = name + " " + Utils.utcDifferenceFromFirstCity(model.getUtc_offset(), firstZoneUTC);
                    }
                    zoneViewHolder.tvTimeDifferenceFromFirstCityId.setText(diff);

                    //-----------------CheckBox Checker--------------//
                    //Show TimeZone//
                    if (isTimeZone) {
                        zoneViewHolder.tvUtcTimeZoneId.setVisibility(View.VISIBLE);
                        zoneViewHolder.tvLocalUtcTimeZoneId.setVisibility(View.VISIBLE);
                    } else {
                        zoneViewHolder.tvUtcTimeZoneId.setVisibility(View.GONE);
                        zoneViewHolder.tvLocalUtcTimeZoneId.setVisibility(View.GONE);
                    }

                    //Show Holidays and Office Hours//
                    if (isHoliday) {
                        zoneViewHolder.tvWorkState.setVisibility(View.VISIBLE);
                    } else {
                        zoneViewHolder.tvWorkState.setVisibility(View.GONE);
                    }

                    //Show Current Time//
                    if (isCurrentTime) {
                        zoneViewHolder.tvCurrentTimeId.setTimeZone(model.getTimezone());
                        zoneViewHolder.tvCurrentTimeId.setVisibility(View.VISIBLE);

                        if (isTimeDateFormat) {
                            zoneViewHolder.tvCurrentTimeId.setFormat12Hour(null);
                            zoneViewHolder.tvCurrentTimeId.setFormat24Hour("hh:mm a");
                        } else {
                            zoneViewHolder.tvCurrentTimeId.setFormat24Hour(null);
                            zoneViewHolder.tvCurrentTimeId.setFormat12Hour("HH:mm");
                        }

                    } else {
                        zoneViewHolder.tvCurrentTimeId.setVisibility(View.GONE);
                    }

                    //Show Time Difference from 1st City/Time Zone//
                    if (isTimeDifference) {
                        zoneViewHolder.tvTimeDifferenceFromFirstCityId.setVisibility(View.VISIBLE);
                    } else {
                        zoneViewHolder.tvTimeDifferenceFromFirstCityId.setVisibility(View.GONE);
                    }

                    //set click listener for time and date//
                    zoneViewHolder.tvDateId.setOnClickListener(view -> {
                        setListener.onSetDateTimeClick(holder.itemView, model, position);
                    });

                    zoneViewHolder.tvUtcTimeDifferenceId.setOnClickListener(view -> {
                        setListener.onSetDateTimeClick(holder.itemView, model, position);
                    });

                    //Item long Pressed delete listener//
                    zoneViewHolder.itemView.setOnLongClickListener(view -> {
                        listener.onItemDeleteClick(model);
                        return false;
                    });
                }
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Object> newZoneList) {
        zoneList.clear();
        //zone add top to bottom//
        zoneList.addAll(newZoneList);

        //zone add bottom to top//
        /*
        for (TimeZoneListModel i : newZoneList) {
            zoneList.add(0, i);
        }
        */
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || zoneList.get(position) instanceof TimeZoneListModel) {
            return ITEM_TYPE_COUNTRY;
        } else {
            return (position % MainActivity.ITEMS_PER_AD == 0) ? ITEM_TYPE_BANNER_AD : ITEM_TYPE_COUNTRY;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ZoneViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView tvCityId;
        TextView tvUtcTimeZoneId;
        TextView tvLocalUtcTimeZoneId;
        TextView tvTimeDifferenceFromFirstCityId;
        TextView tvDateId;
        TextView tvUtcTimeDifferenceId;
        MyTextClock tvCurrentTimeId;
        TextView tvWorkState;
        ImageView imgDayNight;
        ImageView imgDeleteId;
        ImageView country_flag;

        ZoneViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.utcTimeDateContainerLayout);
            tvCityId = itemView.findViewById(R.id.tvCityId);
            tvUtcTimeZoneId = itemView.findViewById(R.id.tvUtcTime);
            tvLocalUtcTimeZoneId = itemView.findViewById(R.id.tvLocalUtcTime);
            tvTimeDifferenceFromFirstCityId = itemView.findViewById(R.id.tvTimeDifferenceFromFirstCityId);
            tvDateId = itemView.findViewById(R.id.tvDateId);
            tvUtcTimeDifferenceId = itemView.findViewById(R.id.tvUtcTimeDifferenceId);
            tvCurrentTimeId = itemView.findViewById(R.id.tvCurrentTimeId);
            tvWorkState = itemView.findViewById(R.id.tvWorkState);
            imgDayNight = itemView.findViewById(R.id.imgDayNight);
            country_flag = itemView.findViewById(R.id.country_flag);
        }
    }

    //Banner Ad View Holder
    static class AdsViewHolder extends RecyclerView.ViewHolder {
        AdsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
