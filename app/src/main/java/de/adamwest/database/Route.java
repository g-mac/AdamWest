package de.adamwest.database;

import java.util.List;
import de.adamwest.database.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ROUTE.
 */
public class Route {

    private Long id;
    /** Not-null value. */
    private String name;
    /** Not-null value. */
    private java.util.Date createdAt;
    private String description;
    private Long holidayId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RouteDao myDao;

    private List<RouteLocation> routeLocationList;
    private List<Event> eventList;

    public Route() {
    }

    public Route(Long id) {
        this.id = id;
    }

    public Route(Long id, String name, java.util.Date createdAt, String description, Long holidayId) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.description = description;
        this.holidayId = holidayId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRouteDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    /** Not-null value. */
    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Long holidayId) {
        this.holidayId = holidayId;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<RouteLocation> getRouteLocationList() {
        if (routeLocationList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RouteLocationDao targetDao = daoSession.getRouteLocationDao();
            List<RouteLocation> routeLocationListNew = targetDao._queryRoute_RouteLocationList(id);
            synchronized (this) {
                if(routeLocationList == null) {
                    routeLocationList = routeLocationListNew;
                }
            }
        }
        return routeLocationList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetRouteLocationList() {
        routeLocationList = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Event> getEventList() {
        if (eventList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventDao targetDao = daoSession.getEventDao();
            List<Event> eventListNew = targetDao._queryRoute_EventList(id);
            synchronized (this) {
                if(eventList == null) {
                    eventList = eventListNew;
                }
            }
        }
        return eventList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetEventList() {
        eventList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
