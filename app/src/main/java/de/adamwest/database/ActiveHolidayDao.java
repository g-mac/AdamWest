package de.adamwest.database;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import de.adamwest.database.ActiveHoliday;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ACTIVE_HOLIDAY.
*/
public class ActiveHolidayDao extends AbstractDao<ActiveHoliday, String> {

    public static final String TABLENAME = "ACTIVE_HOLIDAY";

    /**
     * Properties of entity ActiveHoliday.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Active = new Property(0, String.class, "active", true, "ACTIVE");
        public final static Property HolidayId = new Property(1, Long.class, "holidayId", false, "HOLIDAY_ID");
    };

    private DaoSession daoSession;


    public ActiveHolidayDao(DaoConfig config) {
        super(config);
    }
    
    public ActiveHolidayDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ACTIVE_HOLIDAY' (" + //
                "'ACTIVE' TEXT PRIMARY KEY NOT NULL ," + // 0: active
                "'HOLIDAY_ID' INTEGER);"); // 1: holidayId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ACTIVE_HOLIDAY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ActiveHoliday entity) {
        stmt.clearBindings();
 
        String active = entity.getActive();
        if (active != null) {
            stmt.bindString(1, active);
        }
 
        Long holidayId = entity.getHolidayId();
        if (holidayId != null) {
            stmt.bindLong(2, holidayId);
        }
    }

    @Override
    protected void attachEntity(ActiveHoliday entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ActiveHoliday readEntity(Cursor cursor, int offset) {
        ActiveHoliday entity = new ActiveHoliday( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // active
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1) // holidayId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ActiveHoliday entity, int offset) {
        entity.setActive(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setHolidayId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(ActiveHoliday entity, long rowId) {
        return entity.getActive();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(ActiveHoliday entity) {
        if(entity != null) {
            return entity.getActive();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getHolidayDao().getAllColumns());
            builder.append(" FROM ACTIVE_HOLIDAY T");
            builder.append(" LEFT JOIN HOLIDAY T0 ON T.'HOLIDAY_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ActiveHoliday loadCurrentDeep(Cursor cursor, boolean lock) {
        ActiveHoliday entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Holiday holiday = loadCurrentOther(daoSession.getHolidayDao(), cursor, offset);
        entity.setHoliday(holiday);

        return entity;    
    }

    public ActiveHoliday loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ActiveHoliday> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ActiveHoliday> list = new ArrayList<ActiveHoliday>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ActiveHoliday> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ActiveHoliday> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
