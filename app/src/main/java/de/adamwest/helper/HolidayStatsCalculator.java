package de.adamwest.helper;

import android.location.Location;
import de.adamwest.database.*;

/**
 * Created by philip on 24/10/14.
 */
public final class HolidayStatsCalculator {
    private HolidayStatsCalculator() {}

    public static long getAbsoluteDistanceForHoliday(Holiday holiday) {
        long distance = 0;

        for(Route route : holiday.getRouteList()) {
            distance += getDistanceForRoute(route);
        }

        return  distance;
    }

    public static long getDistanceForRoute(Route route) {
        long distance = 0;
        for(int i = 1; i < route.getRouteLocationList().size(); i++) {
            RouteLocation prevRouteLocation = route.getRouteLocationList().get(i - 1);
            RouteLocation currentRouteLocation = route.getRouteLocationList().get(i);

            Location prevLocation = new Location("dummy_provider");
            prevLocation.setLatitude(prevRouteLocation.getLatitude());
            prevLocation.setLongitude(prevRouteLocation.getLongitude());

            Location currentLocation = new Location("another_dummy_provider");
            currentLocation.setLatitude(currentRouteLocation.getLatitude());
            currentLocation.setLongitude(currentRouteLocation.getLongitude());

            distance += prevLocation.distanceTo(currentLocation);
        }
        return distance;
    }

    public static int getAmountOfImagesForHoliday(Holiday holiday) {
        return getAmountOfMediaFileForHoliday(holiday, Constants.TYPE_IMAGE);
    }

    public static int getAmountOfVideosForHoliday(Holiday holiday) {
        return getAmountOfMediaFileForHoliday(holiday, Constants.TYPE_VIDEO);
    }

    public static int getAmountOfTextsForHoliday(Holiday holiday) {
        return getAmountOfMediaFileForHoliday(holiday, Constants.TYPE_TEXT);
    }

    private static int getAmountOfMediaFileForHoliday(Holiday holiday, String type) {
        int amount = 0;
        for(Route route : holiday.getRouteList()) {
            amount += getAmountOfMediaFileForRoute(route, type);
        }

        return amount;
    }

    public static int getAmountOfMediaFileForRoute(Route route, String type) {
        int amount = 0;
        for(Event event : route.getEventList()) {
            amount+= getAmountOfMediaFileForEvent(event, type);
        }
        return amount;
    }

    public static int getAmountOfMediaFileForEvent(Event event, String type) {
        int amount = 0;
        for(MultimediaElement elem : event.getMultimediaElementList()) {
            if(elem.getType().equals(type)) amount ++;
        }
        return amount;
    }
}
