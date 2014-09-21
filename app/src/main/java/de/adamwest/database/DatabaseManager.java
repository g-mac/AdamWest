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

    public static Route getRouteFromId(Context context, long routeId) {
        return getDaoSession(context).getRouteDao().load(routeId);
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

    public long addEventToRoute(Context context, long routeId, String eventName, String eventDescription, LatLng loc) {
        Route route = getDaoSession(context).getRouteDao().load(routeId);
        if(route == null) {
            //TODO Exception handling
            return -1;
        }

        RouteLocation location = new RouteLocation();
        location.setLatitude(loc.latitude);
        location.setLongitude(loc.longitude);
        location.setCreatedAt(new Date());

        Event event = new Event();
        if(eventName != null) event.setName(eventName);
        if(eventDescription != null) event.setDescription(eventDescription);
        event.setRouteLocation(location);
        event.setRouteId(route.getId());

        long eventId = getDaoSession(context).getEventDao().insert(event);
        getDaoSession(context).getRouteLocationDao().insert(location);

        route.getEventList().add(event);
        getDaoSession(context).getRouteDao().update(route);

        return eventId;
    }



}
