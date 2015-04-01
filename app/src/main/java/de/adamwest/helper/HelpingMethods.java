package de.adamwest.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
