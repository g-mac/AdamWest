package de.adamwest.helper;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by philip on 22/09/14.
 */
public final class HelpingMethods {
    private HelpingMethods() {}
    private static String DEBUG_TAG = "prose";

    public static void log(String msg) {
        Log.d(DEBUG_TAG, msg);
    }


    public static String convertDateToFormattedString(Date date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        return df.format(date);
    }

    public static String convertDateToFormatedTime(Date date) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(date);
    }
}
