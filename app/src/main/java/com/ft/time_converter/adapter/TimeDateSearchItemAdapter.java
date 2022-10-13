package com.ft.time_converter.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ft.time_converter.R;
import com.ft.time_converter.listener.OnItemClickListener;
import com.ft.time_converter.modles.TimeZoneSearchListModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TimeDateSearchItemAdapter extends RecyclerView.Adapter<TimeDateSearchItemAdapter.ViewHolder> {

    private List<TimeZoneSearchListModel> zoneList;
    private Context context;
    private final OnItemClickListener listener;

    public TimeDateSearchItemAdapter(List<TimeZoneSearchListModel> zoneList, OnItemClickListener listener, Context context) {
        this.zoneList = zoneList;
        this.listener = listener;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<TimeZoneSearchListModel> filterList) {
        zoneList.clear();
        zoneList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zone, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TimeDateSearchItemAdapter.ViewHolder holder, int position) {
        TimeZoneSearchListModel model = zoneList.get(position);
        if (model.getCountry().isEmpty()) {
            holder.tv_city_name.setText(model.getTitle() + " (time zone)");
            holder.tv_country_name_id.setText("");
        } else {
            holder.tv_country_name_id.setText(", " + model.getCountry());
            holder.tv_city_name.setText(model.getCity());
        }
        if (model.getParent_code().isEmpty()) {
            holder.tv_utc_name_id.setText("(" + model.getUtc_offset() + ") - " + model.getCode());
        } else {
            holder.tv_utc_name_id.setText("(" + model.getCode() + "/" + model.getParent_code() + ")");
        }

        if (model.getIso2().isEmpty()) {
            //holder.flag_img_view_id.setImageResource(R.drawable.exclamation_mark);
            holder.flag_img_view_id.setImageResource(android.R.color.transparent);
        } else {
            try {
                InputStream inputstream = context.getAssets().open("flags_png/" + model.getIso2().toLowerCase() + ".png");
                Drawable drawable = Drawable.createFromStream(inputstream, null);
                holder.flag_img_view_id.setImageDrawable(drawable);
                drawable = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        holder.itemView.setOnClickListener(view -> listener.onItemClick(view, model, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_city_name;
        TextView tv_country_name_id;
        TextView tv_utc_name_id;
        ImageView flag_img_view_id;

        public ViewHolder(View view) {
            super(view);
            tv_city_name = view.findViewById(R.id.tv_city_name_id);
            tv_country_name_id = view.findViewById(R.id.tv_country_name_id);
            tv_utc_name_id = view.findViewById(R.id.tv_utc_name_id);
            flag_img_view_id = view.findViewById(R.id.flag_img_view_id);
        }
    }
}
