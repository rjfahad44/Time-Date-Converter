package com.ft.time_converter.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ft.time_converter.modles.TimeZoneListModel;

import java.util.List;

@Dao
public interface TimeDateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TimeZoneListModel model);

    @Update
    void update(TimeZoneListModel model);

    @Delete
    void delete(TimeZoneListModel model);

    @Query("DELETE FROM time_zone_list_table")
    void deleteAllTimeZones();

    @Query("SELECT * FROM time_zone_list_table")
    LiveData<List<TimeZoneListModel>> getAllData();

    @Query("SELECT * FROM time_zone_list_table")
    List<TimeZoneListModel> getAllListData();

    @Query("SELECT * FROM time_zone_list_table LIMIT 1")
    List<TimeZoneListModel> checkDb();

    @Query("SELECT * FROM time_zone_list_table WHERE city LIKE :searchQuery AND country LIKE :country OR title LIKE :title")
    List<TimeZoneListModel> findByCity(String searchQuery, String country, String title);
}
