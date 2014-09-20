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

    public static long createNewRoute(Context context, String name, String description) {

        if(name == null) {
            //TODO Exception handling
            return -1;
        }
        Route route = new Route();
        route.setName(name);
        route.setCreatedAt(new Date());
        if(description != null) route.setDescription(description);

        return getDaoSession(context).insert(route);
    }

    public static Route getRouteFromId(Context context, long routeId) {
        return getDaoSession(context).getRouteDao().load(routeId);
    }

    public static List<Route> getAllRoutes(Context context) {
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

        route.getRouteLocationList().add(location);

        getDaoSession(context).getRouteLocationDao().insert(location);
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
        location.setEventId(eventId);
        getDaoSession(context).getRouteLocationDao().insert(location);

        route.getEventList().add(event);
        getDaoSession(context).getRouteDao().update(route);

        return eventId;
    }



}
