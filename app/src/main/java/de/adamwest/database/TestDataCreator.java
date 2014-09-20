package de.adamwest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

/**
 * Created by philip on 17/09/14.
 */
public class TestDataCreator {
    public static DaoSession daoSession;

    public static void setupDatabase(Context context) {
        DaoMaster.DevOpenHelper helper;
        helper = new DaoMaster.DevOpenHelper(context, "holiday-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static void createNewRoutes(Context context) {

        Route firstRoute = new Route();
        firstRoute.setName("Hamburg");
        firstRoute.setCreatedAt(new Date());
        Location loc = new Location();
        loc.setLatitude((long) 10);
        loc.setLongitude((long) 20);
        loc.setRouteId(firstRoute.getId());
        daoSession.insert(firstRoute);
        daoSession.insert(loc);

        daoSession.refresh(firstRoute);
        List list = daoSession.getRouteDao().load(firstRoute.getId()).getLocationList();
        list.add(loc);
        daoSession.update(daoSession.getRouteDao().load(firstRoute.getId()));



        daoSession.getRouteDao().insertOrReplace(firstRoute);
        Route loadedRoute = getAllRoutes(context).get(0);
        //daoSession.getRouteDao().insertOrReplace(secondRoute);
    }

    public static List<Route> getAllRoutes(Context context) {
        return daoSession.getRouteDao().loadAll();
    }

    public static List<Location> getAllLocations() {
        return daoSession.getLocationDao().loadAll();
    }
}
