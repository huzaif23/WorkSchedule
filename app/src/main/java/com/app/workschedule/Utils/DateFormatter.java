package com.app.workschedule.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    private static DateFormatter instance;

    private DateFormatter() {

    }

    public static synchronized DateFormatter getInstance() {
        if (instance == null)
            instance = new DateFormatter();
        return instance;
    }

    public String getTimeStampForFileName() { return new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date()); }

//    public String fomratDateWithDayName(String date) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
//
//    }

    public String formatDateWithDay(String _Date) {
        final String oldFormat = "MM/dd/yyyy";
        final String newFormat = "EEE, dd-MMM-yyyy";
        String newDate="";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldFormat);
            Date date = simpleDateFormat.parse(_Date);
            simpleDateFormat.applyPattern(newFormat);
            newDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public String formatTimeTo12Hr(String time) {
        final String oldFormat = "HH:mm";
        final String newFormat = "hh:mm a";
        String newDate="";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldFormat);
            Date date = simpleDateFormat.parse(time);
            simpleDateFormat.applyPattern(newFormat);
            newDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
}
