package com.vadimmelnicuk.wmgdsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by vadimmelnicuk on 16/08/16.
 */
public class DbDsmHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dsm.db";

    public static final String TABLE_SESSION = "sessions";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_MODULES_EMPATICAE4 = "empaticaE4";
    public static final String COLUMN_MODULES_POLARH7 = "polarH7";
    public static final String COLUMN_MODULES_AFFECTIVA = "affectiva";

    private static final String SQL_CREATE_SESSION_TABLE = "CREATE TABLE " + TABLE_SESSION + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
            COLUMN_MODULES_EMPATICAE4 + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_POLARH7 + " INTEGER DEFAULT 0, " +
            COLUMN_MODULES_AFFECTIVA + " INTEGER DEFAULT 0" +
            ");";

    public DbDsmHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SESSION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void dropDb(Context context) {
        Log.w("data", "Dropping Database: " + DATABASE_NAME);
        context.deleteDatabase(DATABASE_NAME);
    }

    public long startSession() {
        SQLiteDatabase db = this.getWritableDatabase();
        long currentTime = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMESTAMP, currentTime);
        values.put(COLUMN_MODULES_EMPATICAE4, Boolean.compare(Main.modulesEmpaticaE4, false));
        values.put(COLUMN_MODULES_POLARH7, Boolean.compare(Main.modulesPolarH7, false));
        values.put(COLUMN_MODULES_AFFECTIVA, Boolean.compare(Main.modulesAffectiva, false));

        long sessionId = db.insert(TABLE_SESSION, null, values);
        long session = getSessionTimestamp(sessionId);
        return session;
    }

    public Cursor getSessionsCursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SESSION + " ORDER BY " + COLUMN_ID + " DESC", null);
        return cursor;
    }

    public long getSessionTimestamp(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SESSION + " WHERE _id = ?", new String[] {Long.toString(id)});
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            long session = cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP));
            return session;
        }
        return 0;
    }

    public String timestampToDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("yyyyy-MM-dd HH:mm:ss", cal).toString();
        return date;
    }
}
