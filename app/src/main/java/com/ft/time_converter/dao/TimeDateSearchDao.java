package com.ft.time_converter.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ft.time_converter.modles.TimeZoneSearchListModel;

import java.util.List;

@Dao
public interface TimeDateSearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSearchDataList(TimeZoneSearchListModel... model);

    @Update
    void updateSearchDataList(TimeZoneSearchListModel model);

    @Delete
    void deleteSearchDataList(TimeZoneSearchListModel model);

    @Query("DELETE FROM time_zone_search_list_table")
    void deleteAllData();

    @Query("SELECT * FROM time_zone_search_list_table")
    List<TimeZoneSearchListModel> findAllData();

    @Query("SELECT * FROM time_zone_search_list_table LIMIT 1")
    List<TimeZoneSearchListModel> checkSearchDb();

    //ORDER BY city OR country OR title OR location ASC//
    @Query("SELECT * FROM time_zone_search_list_table WHERE title LIKE :searchQuery  || '%' OR city LIKE :searchQuery || '%' OR country LIKE :searchQuery || '%' OR iso2 LIKE :searchQuery || '%' OR iso3 LIKE :searchQuery || '%' OR code LIKE :searchQuery || '%' OR parent_code LIKE :searchQuery || '%' OR location LIKE :searchQuery || '%' LIMIT 10")
    List<TimeZoneSearchListModel> findBySearchText(String searchQuery);
}
