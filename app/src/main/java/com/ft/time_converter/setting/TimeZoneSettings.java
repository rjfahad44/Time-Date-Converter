package com.ft.time_converter.setting;

import android.app.Application;

public class TimeZoneSettings extends Application {

    public final static String PREFERENCE = "preference";

    public final static String UTC_TIME = "utcTime";
    public final static String TIME_ZONE = "timeZone";
    public final static String HOLIDAYS_AND_OFFICE = "holidayAndOffice";
    public final static String CURRENT_TIME = "currentTime";
    public final static String TIME_DIFFERENCE = "timeDifference";
    public final static String ICON_SWITCH = "iconSwitch";

    public final static String UTC_TIME_ON = "utcTimeOn";
    public final static String UTC_TIME_OFF = "utcTimeOff";
    public final static String TIME_ZONE_ON = "timeZoneOn";
    public final static String TIME_ZONE_OFF = "timeZoneOff";
    public final static String HOLIDAYS_AND_OFFICE_ON = "holidaysAndOfficeOn";
    public final static String HOLIDAYS_AND_OFFICE_OFF = "holidaysAndOfficeOff";
    public final static String CURRENT_TIME_ON = "currentTimeOn";
    public final static String CURRENT_TIME_OFF = "currentTimeOff";
    public final static String TIME_DIFFERENCE_ON = "timeDifferenceOn";
    public final static String TIME_DIFFERENCE_OFF = "timeDifferenceOff";
    public final static String ICON_SWITCH_12 = "iconSwitch12";
    public final static String ICON_SWITCH_24 = "iconSwitch24";

    private String utcTimeSettings;
    private String timeZoneSettings;
    private String holidaysAndOfficeSettings;
    private String currentTimeSettings;
    private String TimeDifferenceSettings;
    private String timeFormatSettings;

    public String getUtcTimeSettings() {
        return utcTimeSettings;
    }

    public void setUtcTimeSettings(String utcTimeSettings) {
        this.utcTimeSettings = utcTimeSettings;
    }

    public String getTimeZoneSettings() {
        return timeZoneSettings;
    }

    public void setTimeZoneSettings(String timeZoneSettings) {
        this.timeZoneSettings = timeZoneSettings;
    }

    public String getHolidaysAndOfficeSettings() {
        return holidaysAndOfficeSettings;
    }

    public void setHolidaysAndOfficeSettings(String holidaysAndOfficeSettings) {
        this.holidaysAndOfficeSettings = holidaysAndOfficeSettings;
    }

    public String getCurrentTimeSettings() {
        return currentTimeSettings;
    }

    public void setCurrentTimeSettings(String currentTimeSettings) {
        this.currentTimeSettings = currentTimeSettings;
    }

    public String getTimeDifferenceSettings() {
        return TimeDifferenceSettings;
    }

    public void setTimeDifferenceSettings(String timeDifferenceSettings) {
        TimeDifferenceSettings = timeDifferenceSettings;
    }

    public String getTimeFormatSettings() {
        return timeFormatSettings;
    }

    public void setTimeFormatSettings(String timeFormatSettings) {
        this.timeFormatSettings = timeFormatSettings;
    }
}
