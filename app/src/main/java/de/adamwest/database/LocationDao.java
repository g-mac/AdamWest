package de.adamwest.database;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import de.adamwest.database.Location;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LOCATION.
*/
public class LocationDao extends AbstractDao<Location, Long> {

    public static final String TABLENAME = "LOCATION";

    /**
     * Properties of entity Location.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Latitude = new Property(1, Long.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(2, Long.class, "longitude", false, "LONGITUDE");
        public final static Property CreatedAt = new Property(3, java.util.Date.class, "createdAt", false, "CREATED_AT");
        public final static Property RouteId = new Property(4, Long.class, "routeId", false, "ROUTE_ID");
    };

    private Query<Location> route_LocationListQuery;

    public LocationDao(DaoConfig config) {
        super(config);
    }
    
    public LocationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LOCATION' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'LATITUDE' INTEGER," + // 1: latitude
                "'LONGITUDE' INTEGER," + // 2: longitude
                "'CREATED_AT' INTEGER," + // 3: createdAt
                "'ROUTE_ID' INTEGER);"); // 4: routeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LOCATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Location entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindLong(2, latitude);
        }
 
        Long longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindLong(3, longitude);
        }
 
        java.util.Date createdAt = entity.getCreatedAt();
        if (createdAt != null) {
            stmt.bindLong(4, createdAt.getTime());
        }
 
        Long routeId = entity.getRouteId();
        if (routeId != null) {
            stmt.bindLong(5, routeId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Location readEntity(Cursor cursor, int offset) {
        Location entity = new Location( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // latitude
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // longitude
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // createdAt
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // routeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Location entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLatitude(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setLongitude(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setCreatedAt(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setRouteId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Location entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Location entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "locationList" to-many relationship of Route. */
    public List<Location> _queryRoute_LocationList(Long routeId) {
        synchronized (this) {
            if (route_LocationListQuery == null) {
                QueryBuilder<Location> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RouteId.eq(null));
                route_LocationListQuery = queryBuilder.build();
            }
        }
        Query<Location> query = route_LocationListQuery.forCurrentThread();
        query.setParameter(0, routeId);
        return query.list();
    }

}
