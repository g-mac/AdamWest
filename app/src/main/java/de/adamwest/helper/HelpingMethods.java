package de.adamwest.helper;

import android.content.Context;
import android.util.Log;

/**
 * Created by philip on 22/09/14.
 */
public final class HelpingMethods {
    private HelpingMethods() {}
    private static String DEBUG_TAG = "prose";

    public static void log(String msg) {
        Log.d(DEBUG_TAG, msg);
    }
}
