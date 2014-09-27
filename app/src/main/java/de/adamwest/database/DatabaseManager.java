package de.adamwest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by philip on 20/09/14.
 */
public final class DatabaseManager {
    private DatabaseManager(){}

    //never use the daoSession directly, always use the getter
    private static DaoSession daoSession;


    private static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            DaoMaster.DevOpenHelper helper;
            helper = new DaoMaster.DevOpenHelper(context, "holiday-db", null);
            SQLiteDatabase db = helper.getWritableDatabase();

            DaoMaster daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }

        return daoSession;
    }

    public static long createNewHoliday(Context context, String name, String description) {
        if(name == null) {
            //TODO Exception handling
            return -1;
        }
        Holiday holiday = new Holiday();
        holiday.setName(name);
        holiday.setCreatedAt(new Date());
        if(description != null) holiday.setDescription(description);

        return getDaoSession(context).insert(holiday);
    }

    public static Holiday getHolidayFromId(Context context, long holidayId) {
        return getDaoSession(context).getHolidayDao().load(holidayId);
    }

    public static List<Holiday> getAllHoliday(Context context) {
        return getDaoSession(context).getHolidayDao().loadAll();
    }

    public static long createNewRoute(Context context, long holidayId, String name, String description) {
        Holiday holiday = getHolidayFromId(context, holidayId);

        if(name == null || holiday == null) {
            //TODO Exception handling
            return -1;
        }
        Route route = new Route();
        route.setName(name);
        route.setCreatedAt(new Date());
        if(description != null) route.setDescription(description);
        route.setHolidayId(holiday.getId());
        long routeId = getDaoSession(context).insert(route);
        holiday.getRouteList().add(route);
        getDaoSession(context).update(holiday);
        return routeId;
    }

    public static void setActiveRouteForHoliday(Context context, long holidayId, long routeId) {
        Holiday holiday = getHolidayFromId(context, holidayId);
        holiday.setCurrentRouteId(routeId);
        Route route = getRouteFromId(context, routeId);
        if(route != null) holiday.setCurrentRoute(route);
        getDaoSession(context).update(holiday);
    }

    public static void removeActiveRouteForHoliday(Context context, long holidayId){
        Holiday holiday = getHolidayFromId(context, holidayId);
        holiday.setCurrentRouteId(null);
//        Route route = getRouteFromId(context, routeId);
//        if(route != null) holiday.setCurrentRoute(null);
        holiday.setCurrentRoute(null);
        getDaoSession(context).update(holiday);
    }

    public static Route getRouteFromId(Context context, long routeId) {
        return getDaoSession(context).getRouteDao().load(routeId);
    }

    public static Event getEventFromId(Context context, long eventId) {
        return getDaoSession(context).getEventDao().load(eventId);
    }

    private static List<Route> getAllRoutes(Context context) {
        return getDaoSession(context).getRouteDao().loadAll();
    }

    public void deleteRoute(Context context, long routeId) {
        getDaoSession(context).getRouteDao().deleteByKey(routeId);
    }

    public static void addLocationToRoute(Context context, long routeId, LatLng loc) {
        Route route = getDaoSession(context).getRouteDao().load(routeId);
        if(route == null) {
            //TODO Exception handling
            return;
        }
        RouteLocation location = new RouteLocation();
        location.setLatitude(loc.latitude);
        location.setLongitude(loc.longitude);
        location.setCreatedAt(new Date());
        location.setRouteId(route.getId());

        getDaoSession(context).insert(location);
        route.getRouteLocationList().add(location);
        getDaoSession(context).getRouteDao().update(route);
    }

    public static long createNewEvent(Context context, long routeId, String eventName, String eventDescription, LatLng loc) {
        Route route = getDaoSession(context).getRouteDao().load(routeId);
        if(route == null) {
            //TODO Exception handling
            return -1;
        }

        RouteLocation location = new RouteLocation();
        location.setLatitude(loc.latitude);
        location.setLongitude(loc.longitude);
        location.setCreatedAt(new Date());
        getDaoSession(context).insert(location);

        Event event = new Event();
        if(eventName != null) event.setName(eventName);
        if(eventDescription != null) event.setDescription(eventDescription);
        event.setRouteLocation(location);
        event.setRouteId(route.getId());

        long eventId = getDaoSession(context).insert(event);

        route.getEventList().add(event);
        getDaoSession(context).update(route);

        return eventId;
    }

    public static long createNewEventWithMultiMediaElement(Context context, long routeId, String type, String path, LatLng loc) {
        long eventId = createNewEvent(context, routeId, "", "", loc);
        return createNewMultiMediaElement(context, type, path, eventId);
    }

    public static long createNewMultiMediaElement(Context context, String type, String path, long eventId) {
        MultimediaElement multimediaElement = new MultimediaElement();
        multimediaElement.setType(type);
        multimediaElement.setCreatedAt(new Date());
        multimediaElement.setPath(path);
        multimediaElement.setEventId(eventId);

        Event event = getDaoSession(context).getEventDao().load(eventId);
        event.getMultimediaElementList().add(multimediaElement);

        return getDaoSession(context).insert(multimediaElement);
    }



}
