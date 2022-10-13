package com.ft.time_converter.json;

import android.content.Context;

import com.ft.time_converter.modles.TimeZoneSearchListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class JsonReader {
    public static String lodJsonFromAssets(Context context) {
        String json = null;
        try {
            InputStream in = context.getAssets().open("timezone.json");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
