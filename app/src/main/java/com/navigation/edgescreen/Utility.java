package com.navigation.edgescreen;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.navigation.edgescreen.data.EventPlanner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Utility {

    public static ArrayList<EventPlanner> readCalendarEvent(Context context) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        ArrayList<EventPlanner> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        for (int i = 0; i < CNames.length; i++) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(cursor.getString(3)));
            int mMonth = c.get(Calendar.MONTH) + 1;
            int mYear = c.get(Calendar.YEAR);
            if (mMonth == month && mYear == year) {
                list.add(new EventPlanner(cursor.getString(0),cursor.getString(1), Long.parseLong(cursor.getString(3)), Long.parseLong(cursor.getString(4)), cursor.getString(2)));
            }

            CNames[i] = cursor.getString(1);
            cursor.moveToNext();


        }
        return list;
    }


}