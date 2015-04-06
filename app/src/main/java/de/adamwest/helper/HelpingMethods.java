package de.adamwest.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
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

    private HelpingMethods() {
    }

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

    //todo: implement check differently? in databasemanger? performance?
    public static boolean holidayHasLocationPoints(Holiday holiday) {
        boolean result = false;

        for (Route route : holiday.getRouteList())
            result = result || routeHasLocationPoints(route);

        return result;
    }

    //todo: implement check differently? in databasemanger? performance?
    public static boolean routeHasLocationPoints(Route route) {
        boolean result = false;
        result = !(route.getRouteLocationList().isEmpty());
        return result;
    }

    //------------ test data -------------------------------------------------------------------------------------------

    public static void createTestData(Context context) {

        if (!DatabaseManager.getAllHoliday(context).isEmpty())
            return;

        log("creating test data...");

        int holidays = 5;
        int activeHoliday = 3;
        int routes = 8;

        for (int i = 0; i < holidays; i++) {
            long id = DatabaseManager.createNewHoliday(context, "Test Holiday " + i, "description of test holiday number " + i + ".");

            if (i == activeHoliday)
                DatabaseManager.setHolidayAsActive(context, id);
            for (int j = 0; j < routes; j++)
                DatabaseManager.createNewRoute(context, id, "Test Route " + j, "description of test route number " + j + ".");
        }
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
