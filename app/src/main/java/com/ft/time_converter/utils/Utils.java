package com.ft.time_converter.utils;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static String getZoneCurrentDate(String zoneName, Context context) {

        SharedPreferences timeDateSp = context.getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        int hours = timeDateSp.getInt("TIME_HOURS", 0);
        int minutes = timeDateSp.getInt("TIME_MINUTES", 0);
        int days = timeDateSp.getInt("DAY", 0);
        int months = timeDateSp.getInt("MONTH", 0);
        int years = timeDateSp.getInt("YEAR", 0);

        if (hours == 0) {
            days = days - 1;
        }
        //Current date and time from the target zone
        Date date = new Date();

        //DateFormat dff = new SimpleDateFormat("E, dd MMM yyyy  HH:mm:ss a");
        @SuppressLint("SimpleDateFormat") DateFormat dff = new SimpleDateFormat("EEEE, dd MMM yyyy");
        //Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(zoneName));
        Calendar calendar = new GregorianCalendar();
        String s;
        if (!timeDateSp.getAll().isEmpty()) {
            calendar.set(years, months, days, hours, minutes);
            s = dff.format(calendar.getTime());
        } else {
            dff.setTimeZone(TimeZone.getTimeZone(zoneName));
            s = dff.format(date);
        }
        return s;
    }

    public static String getUtcTime(Context context) {
        SharedPreferences timeDateSp = context.getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        int utc_hours = timeDateSp.getInt("UTC_HH", 0);
        int utc_minutes = timeDateSp.getInt("UTC_MM", 0);

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dff = new SimpleDateFormat("HH:mm");

        String s;
        if (!timeDateSp.getAll().isEmpty()) {
            if (utc_hours < 10) {
                s = "0" + utc_hours;
            } else {
                s = "" + utc_hours;
            }
            if (utc_minutes < 10) {
                s += ":" + "0" + utc_minutes;
            } else {
                s += ":" + "" + utc_minutes;
            }
        } else {
            dff.setTimeZone(TimeZone.getTimeZone("UTC"));
            s = dff.format(date);

            //update UTC time every 30 minutes//
//            String h, m;
//            h = s.substring(0, 2);
//            m = s.substring(3, 5);
//            s = m.equals("30")? h+":"+m : h+":00";
            //Log.d("UTC_TIME", s);
        }
        return s;
    }

    public static String getUtcDate(Context context) {
        SharedPreferences timeDateSp = context.getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        int hours = timeDateSp.getInt("TIME_HOURS", 0);
        int minutes = timeDateSp.getInt("TIME_MINUTES", 0);
        int days = timeDateSp.getInt("DAY", 0);
        int months = timeDateSp.getInt("MONTH", 0);
        int years = timeDateSp.getInt("YEAR", 0);

        if (hours == 0) {
            days = days - 1;
        }

        Calendar calendar = new GregorianCalendar();
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dff = new SimpleDateFormat("EEEE, dd MMM yyyy");

        String s;
        if (!timeDateSp.getAll().isEmpty()) {
            calendar.set(years, months, days, hours, minutes);
            s = dff.format(calendar.getTime());
        } else {
            dff.setTimeZone(TimeZone.getTimeZone("UTC"));
            s = dff.format(date);
        }
        return s;
    }

    public static String utcDifferenceFromFirstCity(String city, String firstCity) {
        String result = "";
        try {
            int cityHH = (Integer.parseInt(city.substring(1, 3))) * 60 + Integer.parseInt(city.substring(4, 6));
            int firstCityHH = (Integer.parseInt(firstCity.substring(1, 3))) * 60 + Integer.parseInt(firstCity.substring(4, 6));

            if (!(city.charAt(0) == '+')) {
                cityHH = cityHH * (-1);
            }
            if (!(firstCity.charAt(0) == '+')) {
                firstCityHH = firstCityHH * (-1);
            }

            String s = String.valueOf((double) ((cityHH) - (firstCityHH)) / 60);

            if (s.charAt(s.length() - 1) == '5') {
                result = s;
                if (!(result.charAt(0) == '-')) {
                    result = "+" + result;
                }
            } else {
                result = String.valueOf((cityHH - firstCityHH) / 60);
                if (Double.parseDouble(result) >= 0) {
                    result = "+" + result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getUtcTimeCalender(Context context) {
        SharedPreferences timeDateSp = context.getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        int utc_hours = timeDateSp.getInt("UTC_HH", 0);
        int utc_minutes = timeDateSp.getInt("UTC_MM", 0);

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dff = new SimpleDateFormat("HH:mm");

        String s;
        if (!timeDateSp.getAll().isEmpty()) {
            if (utc_hours < 10) {
                s = "0" + utc_hours;
            } else {
                s = "" + utc_hours;
            }
            if (utc_minutes < 10) {
                s += ":" + "0" + utc_minutes;
            } else {
                s += ":" + "" + utc_minutes;
            }
        } else {
            dff.setTimeZone(TimeZone.getTimeZone("UTC"));
            s = dff.format(date);
        }
        return Integer.parseInt(s.substring(0, 2));
    }

    public static Calendar dateTime(String zoneName) {
        return new GregorianCalendar(TimeZone.getTimeZone(zoneName));
    }

    public static String dayOfWeek(Context context, String zoneName) {
        SharedPreferences timeDateSp = context.getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        int days = timeDateSp.getInt("DAY", 0);
        int months = timeDateSp.getInt("MONTH", 0);
        int years = timeDateSp.getInt("YEAR", 0);

        if (timeDateSp.getBoolean("SET_DATE", false)){
            String[] weekend = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
            Calendar cal = Calendar.getInstance();
            cal.set(years,months,days);
            String day = weekend[cal.get(cal.DAY_OF_WEEK)-1];
            Log.d("CHECK_DATE_SET", "set the date : "+ day);
            return day;
        }else {
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(zoneName));
            Date date = calendar.getTime();
            return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        }
    }

    public static String getDifferenceFromUtcInHoursAndMinutes(String utc, Context context) {

        String result = "";
        try {
            int zoneUTCHours = (Integer.parseInt(utc.substring(1, 3)));
            int zoneUTCMinutes = Integer.parseInt(utc.substring(4, 6));

            if (!(utc.charAt(0) == '+')) {
                zoneUTCHours = zoneUTCHours * (-1);
            }

            // UTC Time
            int utcHour = (Utils.getUtcTimeCalender(context) + zoneUTCHours);

            if (utcHour < 0 || utcHour >= 24) {
                if (utcHour < 0) {
                    result = "" + (24 + utcHour) + ":" + (zoneUTCMinutes < 10 ? "0" + zoneUTCMinutes : zoneUTCMinutes);
                } else {
                    result = "0" + (utcHour - 24) + ":" + (zoneUTCMinutes < 10 ? "0" + zoneUTCMinutes : zoneUTCMinutes);
                }
            } else {
                result = "" + (utcHour < 10 ? "0" + utcHour : utcHour) + ":" + (zoneUTCMinutes < 10 ? "0" + zoneUTCMinutes : zoneUTCMinutes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isDayOrNight(String zoneId, String utc, Context context) {
        SharedPreferences timeDateSp = context.getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dff = new SimpleDateFormat("HH:mm a");

        String s;
        if (!timeDateSp.getAll().isEmpty()) {
            s = getDifferenceFromUtcInHoursAndMinutes(utc, context).substring(0, 2);
        } else {
            dff.setTimeZone(TimeZone.getTimeZone(zoneId));
            s = dff.format(date);
        }

        int H = Integer.parseInt(s.substring(0, 2));
        //Log.d("TAG", "isDayOrNight : "+H);
        return H >= 6 && H < 19;
    }

    public static int isWorkState(String zoneId, String utc, Context context) {
        SharedPreferences timeDateSp = context.getSharedPreferences("SET_DATE_TIME", MODE_PRIVATE);
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") DateFormat dff = new SimpleDateFormat("HH:mm a");

        String s;
        if (!timeDateSp.getAll().isEmpty()) {
            s = getDifferenceFromUtcInHoursAndMinutes(utc, context).substring(0, 2);
        } else {
            dff.setTimeZone(TimeZone.getTimeZone(zoneId));
            s = dff.format(date);
        }

        int H = Integer.parseInt(s.substring(0, 2));

        // (1 = Good), (2 = Not So Good), (3 = Not Good) //
        if (H >= 9 && H <= 18) {
            return 1;
        } else if ((H > 18 && H <= 22) || (H >= 7 && H < 9)) {
            return 2;
        } else {
            return 3;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }
}
