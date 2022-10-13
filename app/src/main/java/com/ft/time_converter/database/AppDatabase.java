package com.ft.time_converter.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ft.time_converter.dao.TimeDateDao;
import com.ft.time_converter.dao.TimeDateSearchDao;
import com.ft.time_converter.modles.TimeZoneListModel;
import com.ft.time_converter.modles.TimeZoneSearchListModel;

@Database(entities = {TimeZoneListModel.class, TimeZoneSearchListModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract TimeDateDao timeDateDao();

    public abstract TimeDateSearchDao timeDateSearchDao();


    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "time_date_converter_database")
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }
}
