package de.adamwest.database;

import de.greenrobot.dao.DaoException;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table HOLIDAY.
 */
public class Holiday {

    private Long id;
    private String name;
    private String description;
    private java.util.Date createdAt;
    private Long currentRouteId;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient HolidayDao myDao;

    private Route route;
    private Long route__resolvedKey;

    private List<Route> routeList;

    public Holiday() {
    }

    public Holiday(Long id) {
        this.id = id;
    }

    public Holiday(Long id, String name, String description, java.util.Date createdAt, Long currentRouteId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.currentRouteId = currentRouteId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHolidayDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getCurrentRouteId() {
        if (currentRouteId == null)
            return -1;
        else
            return currentRouteId;
    }

    public void setCurrentRouteId(Long currentRouteId) {
        this.currentRouteId = currentRouteId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Route getRoute() {
        Long __key = this.currentRouteId;
        if (route__resolvedKey == null || !route__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RouteDao targetDao = daoSession.getRouteDao();
            Route routeNew = targetDao.load(__key);
            synchronized (this) {
                route = routeNew;
                route__resolvedKey = __key;
            }
        }
        return route;
    }

    public void setRoute(Route route) {
        synchronized (this) {
            this.route = route;
            currentRouteId = route == null ? null : route.getId();
            route__resolvedKey = currentRouteId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity.
     */
    public List<Route> getRouteList() {
        if (routeList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RouteDao targetDao = daoSession.getRouteDao();
            List<Route> routeListNew = targetDao._queryHoliday_RouteList(id);
            synchronized (this) {
                if (routeList == null) {
                    routeList = routeListNew;
                }
            }
        }
        return routeList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    public synchronized void resetRouteList() {
        routeList = null;
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

}
