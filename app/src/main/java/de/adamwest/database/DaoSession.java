package de.adamwest.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import de.adamwest.database.Route;
import de.adamwest.database.Location;
import de.adamwest.database.Event;
import de.adamwest.database.MultimediaElement;

import de.adamwest.database.RouteDao;
import de.adamwest.database.LocationDao;
import de.adamwest.database.EventDao;
import de.adamwest.database.MultimediaElementDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig routeDaoConfig;
    private final DaoConfig locationDaoConfig;
    private final DaoConfig eventDaoConfig;
    private final DaoConfig multimediaElementDaoConfig;

    private final RouteDao routeDao;
    private final LocationDao locationDao;
    private final EventDao eventDao;
    private final MultimediaElementDao multimediaElementDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        routeDaoConfig = daoConfigMap.get(RouteDao.class).clone();
        routeDaoConfig.initIdentityScope(type);

        locationDaoConfig = daoConfigMap.get(LocationDao.class).clone();
        locationDaoConfig.initIdentityScope(type);

        eventDaoConfig = daoConfigMap.get(EventDao.class).clone();
        eventDaoConfig.initIdentityScope(type);

        multimediaElementDaoConfig = daoConfigMap.get(MultimediaElementDao.class).clone();
        multimediaElementDaoConfig.initIdentityScope(type);

        routeDao = new RouteDao(routeDaoConfig, this);
        locationDao = new LocationDao(locationDaoConfig, this);
        eventDao = new EventDao(eventDaoConfig, this);
        multimediaElementDao = new MultimediaElementDao(multimediaElementDaoConfig, this);

        registerDao(Route.class, routeDao);
        registerDao(Location.class, locationDao);
        registerDao(Event.class, eventDao);
        registerDao(MultimediaElement.class, multimediaElementDao);
    }
    
    public void clear() {
        routeDaoConfig.getIdentityScope().clear();
        locationDaoConfig.getIdentityScope().clear();
        eventDaoConfig.getIdentityScope().clear();
        multimediaElementDaoConfig.getIdentityScope().clear();
    }

    public RouteDao getRouteDao() {
        return routeDao;
    }

    public LocationDao getLocationDao() {
        return locationDao;
    }

    public EventDao getEventDao() {
        return eventDao;
    }

    public MultimediaElementDao getMultimediaElementDao() {
        return multimediaElementDao;
    }

}