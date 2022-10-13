package com.ft.time_converter.modles;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "time_zone_search_list_table")
public class TimeZoneSearchListModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String city, city_ascii, lat, lng, pop, country, iso2, iso3, province, timezone, utc_offset, utc_dst, code, parent_code, title, location, week_end;

    public TimeZoneSearchListModel(String city, String city_ascii, String lat, String lng, String pop, String country, String iso2, String iso3, String province, String timezone, String utc_offset, String utc_dst, String code, String parent_code, String title, String location, String week_end) {
        this.city = city;
        this.city_ascii = city_ascii;
        this.lat = lat;
        this.lng = lng;
        this.pop = pop;
        this.country = country;
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.province = province;
        this.timezone = timezone;
        this.utc_offset = utc_offset;
        this.utc_dst = utc_dst;
        this.code = code;
        this.parent_code = parent_code;
        this.title = title;
        this.location = location;
        this.week_end = week_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_ascii() {
        return city_ascii;
    }

    public void setCity_ascii(String city_ascii) {
        this.city_ascii = city_ascii;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUtc_offset() {
        return utc_offset;
    }

    public void setUtc_offset(String utc_offset) {
        this.utc_offset = utc_offset;
    }

    public String getUtc_dst() {
        return utc_dst;
    }

    public void setUtc_dst(String utc_dst) {
        this.utc_dst = utc_dst;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent_code(){ return parent_code; }

    public void setParent_code(){  this.parent_code = parent_code; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation(){ return location; }

    public void setLocation(){ this.location = location; }

    public String getWeek_end() {
        return week_end;
    }

    public void setWeek_end(String week_end) {
        this.week_end = week_end;
    }
}
