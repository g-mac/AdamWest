package de.adamwest.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import de.adamwest.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.database.RouteLocation;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by philip on 22/09/14.
 */
public final class HelpingMethods {

    private HelpingMethods() {}

    private static String DEBUG_TAG = "prose";

    //------------------------------------------------------------------------------------------------------------------

    public static void log(String msg) {
        Log.d(DEBUG_TAG, msg);
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    //----------- arithmetic and stuff ---------------------------------------------------------------------------------

    public static String convertDateToFormattedString(Date date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        return df.format(date);
    }

    public static String convertDateToFormatedTime(Date date) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(date);
    }

    public static LatLngBounds getRouteBoundaries(Route route) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        List<RouteLocation> locations = route.getRouteLocationList();
        for (RouteLocation location : locations) {
            builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        return builder.build();
    }

    public static LatLngBounds getHolidayBoundaries(Holiday holiday) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        List<Route> routes = holiday.getRouteList();

        for (Route route : routes) {
            List<RouteLocation> locations = route.getRouteLocationList();

            for (RouteLocation location : locations) {
                builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
            }

        }
        return builder.build();
    }

    //------------ test data -------------------------------------------------------------------------------------------

    public static void createTestData(Context context){
        if(!DatabaseManager.getAllHoliday(context).isEmpty())
            return;

        log("creating test data...");
        long id1 = DatabaseManager.createNewHoliday(context, "Test Holiday 1", "description of test holiday one.");
        long id2 = DatabaseManager.createNewHoliday(context, "Test Holiday 2", "description of test holiday two.");
        long id3 = DatabaseManager.createNewHoliday(context, "Test Holiday 3", "description of test holiday three.");
        long id4 = DatabaseManager.createNewHoliday(context, "Test Holiday 4", "description of test holiday four.");
        long id5 = DatabaseManager.createNewHoliday(context, "Test Holiday 5", "description of test holiday five.");

        DatabaseManager.setHolidayAsActive(context, id3);
    }

    //------------ trash -----------------------------------------------------------------------------------------------

//    public static CircleOptions getCircleOptions() {
//        CircleOptions co = new CircleOptions();
////        co.center(latlng);
//        co.radius(100);
////        co.fillColor(Color.RED);
//        co.strokeColor(Color.RED);
//        co.strokeWidth(2.0f);
//        return co;
//    }

}
