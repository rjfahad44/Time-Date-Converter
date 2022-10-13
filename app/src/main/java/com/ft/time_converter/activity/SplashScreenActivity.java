package com.ft.time_converter.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ft.time_converter.R;
import com.ft.time_converter.dao.TimeDateSearchDao;
import com.ft.time_converter.database.AppDatabase;
import com.ft.time_converter.json.JsonReader;
import com.ft.time_converter.modles.TimeZoneSearchListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private TimeDateSearchDao searchDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        searchDao = AppDatabase.getInstance(this).timeDateSearchDao();
        insertDataOneTime();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }, 2500);
    }

    private void insertDataOneTime() {
        List<TimeZoneSearchListModel> listModels = new ArrayList<>();
        if (searchDao.checkSearchDb().isEmpty()) {
            try {
                //city, city_ascii, lat, lng, pop, country, iso2, iso3, province, timezone, utc_offset, utc_dst, code, parent_code, title, location, week_end//
                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(JsonReader.lodJsonFromAssets(getApplicationContext())));
                JSONArray jsonArray = jsonObject.getJSONArray("timeZoneJsonList");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject zone = jsonArray.optJSONObject(i);
                    listModels.add(new TimeZoneSearchListModel(
                            zone.optString("city"),
                            zone.optString("city_ascii"),
                            zone.optString("lat"),
                            zone.optString("lng"),
                            zone.optString("pop"),
                            zone.optString("country"),
                            zone.optString("iso2"),
                            zone.optString("iso3"),
                            zone.optString("province"),
                            zone.optString("timezone"),
                            zone.optString("utc_offset"),
                            zone.optString("utc_dst"),
                            zone.optString("code"),
                            zone.optString("parent_code"),
                            zone.optString("title"),
                            zone.optString("location"),
                            zone.optString("week_end")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            searchDao.insertSearchDataList(listModels.toArray(new TimeZoneSearchListModel[0]));
        }
    }
}