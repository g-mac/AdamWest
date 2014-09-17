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
        firstRoute.setCreated_at(new Date());

        Route secondRoute = new Route();
        secondRoute.setName("Burkinafaso");
        secondRoute.setCreated_at(new Date());
        daoSession.getRouteDao().insertOrReplace(firstRoute);
        daoSession.getRouteDao().insertOrReplace(secondRoute);
    }

    public static List<Route> getAllRoutes(Context context) {
        return daoSession.getRouteDao().loadAll();
    }
}
